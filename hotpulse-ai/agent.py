import json
import os
import re
from dataclasses import dataclass
from typing import Any
from urllib.error import HTTPError, URLError
from urllib.parse import quote
from urllib.request import urlopen

from dotenv import load_dotenv
from openai import OpenAI

load_dotenv()

client = OpenAI(
    api_key=os.getenv("DASHSCOPE_API_KEY"),
    base_url="https://dashscope.aliyuncs.com/compatible-mode/v1",
)

SYSTEM_PROMPT = """你是 Hotpulse Research Agent，小热。
你的任务不是闲聊，而是帮助用户把一个研究题目梳理清楚。

工作方式：
1. 先识别用户想研究什么。
2. 尽量基于已检索到的本地新闻资料进行分析。
3. 输出简洁、结构化、适合产品面板展示的结果。
4. 如果没有检索到资料，也要明确说明是基于上下文的研究整理，不要伪造事实。
5. 尽量给出下一步值得继续追问的方向，让研究可以持续推进。
"""

NEWS_BASE_URL = os.getenv("HOTPULSE_SERVER_BASE_URL", "http://127.0.0.1:8080").rstrip("/")

STOP_WORDS = {
    "帮我",
    "请",
    "一下",
    "这个",
    "这些",
    "那些",
    "今天",
    "最近",
    "最新",
    "研究",
    "分析",
    "总结",
    "归纳",
    "概括",
    "整理",
    "对比",
    "比较",
    "热点",
    "新闻",
    "趋势",
    "问题",
    "如何",
    "什么",
    "为什么",
    "可以",
    "有没有",
    "帮我看",
}


@dataclass
class ResearchProfile:
    research_type: str
    plan: list[str]
    evidence: list[str]
    key_findings: list[str]
    next_steps: list[str]


def _safe_text(value: Any) -> str:
    return str(value).strip() if value is not None else ""


def _normalize_history(history: list) -> list[dict]:
    normalized: list[dict] = []
    for item in history[-12:]:
        if isinstance(item, dict):
            role = item.get("role")
            content = _safe_text(item.get("content"))
        else:
            role = getattr(item, "role", None)
            content = _safe_text(getattr(item, "content", ""))
        if role not in {"user", "assistant"} or not content:
            continue
        normalized.append({"role": role, "content": content})
    return normalized


def _extract_keywords(query: str) -> list[str]:
    tokens = re.findall(r"[\u4e00-\u9fffA-Za-z0-9]{2,}", query)
    keywords: list[str] = []
    for token in tokens:
        if token in STOP_WORDS:
            continue
        if token not in keywords:
            keywords.append(token)
    if not keywords and query.strip():
        keywords.append(query.strip())
    return keywords[:4]


def _fetch_news_by_keyword(keyword: str) -> list[dict]:
    url = f"{NEWS_BASE_URL}/news?keyword={quote(keyword)}"
    try:
        with urlopen(url, timeout=4) as response:
            raw = response.read().decode("utf-8")
            payload = json.loads(raw)
            data = payload.get("data") if isinstance(payload, dict) else payload
            if isinstance(data, list):
                return [item for item in data if isinstance(item, dict)]
    except (HTTPError, URLError, TimeoutError, ValueError, json.JSONDecodeError):
        return []
    except Exception:
        return []
    return []


def _collect_sources(query: str) -> list[dict]:
    collected: dict[Any, dict] = {}
    for keyword in _extract_keywords(query):
        for item in _fetch_news_by_keyword(keyword):
            news_id = item.get("id")
            if news_id in collected:
                continue
            collected[news_id or f"{keyword}:{len(collected)}"] = {
                "id": item.get("id"),
                "title": _safe_text(item.get("title")) or keyword,
                "summary": _safe_text(item.get("summary")),
                "source": _safe_text(item.get("source")),
                "category": _safe_text(item.get("category")),
                "heat": item.get("heat"),
                "published_at": _safe_text(item.get("publishedAt") or item.get("published_at")),
                "keyword": keyword,
            }

    sources = list(collected.values())
    sources.sort(key=lambda item: (item.get("heat") or 0, item.get("published_at") or ""), reverse=True)
    return sources[:5]


def _is_grad_query(query: str) -> bool:
    """Detect if the query is about graduate school recommendations."""
    grad_keywords = ["考研", "硕士", "研究生", "择校", "院校推荐", "报录比", "学硕", "专硕",
                     "复试线", "录取", "报考", "导师", "计算机考研", "电子信息考研"]
    text = query.lower()
    return any(k in text for k in grad_keywords)


def _fetch_grad_recommendations(query: str) -> list[dict]:
    """Call the backend grad recommendation API."""
    import urllib.request
    import urllib.error

    url = f"{NEWS_BASE_URL}/grad/recommend"

    # Extract parameters from query
    payload = {}
    if "计算机" in query or "CS" in query.upper():
        payload["majorKeyword"] = "计算机"
    elif "电子" in query:
        payload["majorKeyword"] = "电子信息"
    elif "软件" in query:
        payload["majorKeyword"] = "软件工程"
    elif "工商" in query or "管理" in query:
        payload["majorKeyword"] = "工商管理"
    elif "法学" in query or "法律" in query:
        payload["majorKeyword"] = "法学"
    elif "临床" in query or "医学" in query:
        payload["majorKeyword"] = "临床医学"

    # Extract score
    import re
    score_match = re.search(r'(\d{3})\s*分', query)
    if score_match:
        payload["score"] = int(score_match.group(1))

    # Extract degree type
    if "专硕" in query or "专业学位" in query:
        payload["degreeType"] = "专硕"
    elif "学硕" in query or "学术学位" in query:
        payload["degreeType"] = "学硕"

    # Extract school tag preference
    if "985" in query:
        payload["schoolTag"] = "985"
    elif "211" in query:
        payload["schoolTag"] = "211"

    # Extract province
    provinces = ["北京", "上海", "四川", "成都", "湖北", "武汉", "江苏", "南京", "陕西", "西安",
                 "广东", "广州", "深圳", "浙江", "杭州"]
    for p in provinces:
        if p in query:
            if p == "成都": payload["province"] = "四川"
            elif p == "武汉": payload["province"] = "湖北"
            elif p == "南京": payload["province"] = "江苏"
            elif p == "西安": payload["province"] = "陕西"
            elif p == "广州" or p == "深圳": payload["province"] = "广东"
            elif p == "杭州": payload["province"] = "浙江"
            else: payload["province"] = p
            break

    try:
        data = json.dumps(payload).encode("utf-8")
        req = urllib.request.Request(url, data=data, headers={"Content-Type": "application/json"})
        with urllib.request.urlopen(req, timeout=6) as resp:
            raw = resp.read().decode("utf-8")
            result = json.loads(raw)
            items = result.get("data", []) if isinstance(result, dict) else []
            return items if isinstance(items, list) else []
    except Exception:
        return []


def _build_grad_context(recommendations: list[dict]) -> str:
    """Format grad recommendations for the LLM prompt."""
    if not recommendations:
        return "未检索到匹配的考研院校数据。"

    lines = ["数据库匹配到的考研院校推荐："]
    for i, r in enumerate(recommendations, 1):
        tag = r.get("riskLevel", "")
        emoji = {"保": "🟢", "稳": "🟡", "冲": "🔴"}.get(tag, "⚪")
        lines.append(
            f"{i}. {emoji} [{tag}] {r.get('schoolName')} · {r.get('majorName')} ({r.get('degreeType') or '学硕'})\n"
            f"   位置：{r.get('city')} | 院校层次：{r.get('schoolTag')}\n"
            f"   复试线：{r.get('totalScoreLine')} | 录取均分：{r.get('avgScore')} | "
            f"报录比：{r.get('admitRatio', '-')}%（{r.get('admitCount')}/{r.get('applicantCount')}）\n"
            f"   考试科目：{r.get('examSubjects')}\n"
            f"   推荐理由：{r.get('reason')}\n"
        )
        tutors = r.get("tutors", [])
        if tutors:
            lines.append(f"   导师：{'; '.join(tutors[:3])}")

    return "\n".join(lines)


def _infer_profile(query: str, source_count: int) -> ResearchProfile:
    text = query.lower()

    # 考研择校
    if _is_grad_query(query):
        return ResearchProfile(
            research_type="考研择校研究",
            plan=["分析用户背景与目标专业", "匹配院校数据库", "按冲/稳/保三档分类推荐", "分析报录比与录取趋势", "给出备考与选择建议"],
            evidence=["本地考研院校数据库", "历年复试线与录取数据"],
            key_findings=["基于真实录取数据做三档推荐", "每所院校给出录取概率评估", "提供导师研究方向参考"],
            next_steps=["调整目标省份或院校层次重新分析", "对比两所院校的详细数据", "查询特定导师的研究方向", "制定备考时间线"],
        )

    if any(key in text for key in ["总结", "归纳", "概括", "摘要"]):
        return ResearchProfile(
            research_type="摘要研究",
            plan=["识别研究主题", "提炼核心观点", "归纳关键信息", "给出可继续追问的方向"],
            evidence=["本地新闻检索结果", "用户描述中的主题线索"] if source_count else ["用户描述中的主题线索"],
            key_findings=["先收敛主题，再输出结构化摘要"],
            next_steps=["扩展某个观点", "按时间线整理", "生成简报版结论"],
        )
    if any(key in text for key in ["对比", "区别", "优缺点", "选型", "方案"]):
        return ResearchProfile(
            research_type="方案研究",
            plan=["拆解对比维度", "归纳差异", "比较优缺点", "输出建议结论"],
            evidence=["本地新闻检索结果", "用户给出的方案背景"] if source_count else ["用户给出的方案背景"],
            key_findings=["先明确比较维度，再谈取舍结论"],
            next_steps=["补充对比维度", "生成表格版对照", "给出推荐方案"],
        )
    if any(key in text for key in ["热点", "新闻", "最新", "今日", "趋势"]):
        return ResearchProfile(
            research_type="热点研究",
            plan=["锁定主题", "整理热点线索", "提炼影响因素", "输出追问路径"],
            evidence=["本地新闻检索结果", "用户输入中的热点主题"] if source_count else ["用户输入中的热点主题"],
            key_findings=["热点问题更适合持续追踪，而不是一次性回答"],
            next_steps=["继续追问细节", "按行业分类", "生成今日简报"],
        )
    if any(key in text for key in ["论文", "报告", "研究", "调研", "分析"]):
        return ResearchProfile(
            research_type="研究助手",
            plan=["明确研究对象", "收集资料线索", "整理核心结论", "输出后续研究问题"],
            evidence=["本地新闻检索结果", "历史对话中的背景信息"] if source_count else ["历史对话中的背景信息"],
            key_findings=["研究任务需要先定义问题边界"],
            next_steps=["补充研究背景", "拆成子问题", "整理成报告结构"],
        )
    return ResearchProfile(
        research_type="研究拆解",
        plan=["理解研究目标", "整理上下文", "拆成研究步骤", "给出下一轮问题建议"],
        evidence=["本地新闻检索结果", "用户当前输入"] if source_count else ["用户当前输入"],
        key_findings=["先把问题拆开，再逐步推进"],
        next_steps=["继续追问", "改成清单", "补充背景材料"],
    )


def _build_messages(query: str, history: list[dict], profile: ResearchProfile, sources: list[dict]) -> list[dict]:
    source_context = "本地新闻资料：\n"
    if sources:
        for index, source in enumerate(sources, start=1):
            source_context += (
                f"{index}. 标题：{source.get('title')}\n"
                f"   来源：{source.get('source') or '-'} | 分类：{source.get('category') or '-'}"
                f" | 热度：{source.get('heat') or '-'} | 发布时间：{source.get('published_at') or '-'}\n"
                f"   摘要：{source.get('summary') or '-'}\n"
            )
    else:
        source_context += "未检索到匹配的本地新闻资料。"

    task_blueprint = (
        f"研究类型：{profile.research_type}\n"
        f"研究步骤：{'; '.join(profile.plan)}\n"
        f"证据线索：{'; '.join(profile.evidence)}\n"
        f"关键发现：{'; '.join(profile.key_findings)}\n"
        f"下一步建议：{'; '.join(profile.next_steps)}\n"
        f"{source_context}"
    )

    messages = [
        {"role": "system", "content": SYSTEM_PROMPT},
        {"role": "system", "content": task_blueprint},
    ]
    messages.extend(history)
    messages.append({"role": "user", "content": query})
    return messages


def _extract_content(response: Any) -> str:
    message = response.choices[0].message
    content = getattr(message, "content", None)
    if isinstance(content, str) and content.strip():
        return content.strip()
    return "我已经完成研究拆解，但当前没有生成可展示的正文内容。"


def run_agent(query: str, history: list) -> dict:
    sources = _collect_sources(query)
    profile = _infer_profile(query, len(sources))
    normalized_history = _normalize_history(history)

    # Check if this is a grad school query
    grad_recommendations = []
    if _is_grad_query(query):
        grad_recommendations = _fetch_grad_recommendations(query)
        # If we got grad data, include it in sources for the LLM context
        if grad_recommendations and not sources:
            sources = [{
                "id": f"grad-{i}",
                "title": s.get("schoolName", ""),
                "summary": f"{s.get('majorName')} | 复试线{s.get('totalScoreLine')} | 均分{s.get('avgScore')} | {s.get('riskLevel')}",
                "source": f"{s.get('city')} · {s.get('schoolTag')}",
                "category": s.get("riskLevel", "考研"),
                "heat": s.get("avgScore") or 0,
                "published_at": "最新年份",
                "keyword": "考研",
            } for i, s in enumerate(grad_recommendations)]

    messages = _build_messages(query, normalized_history, profile, sources)

    # Add grad-specific context to the system messages if applicable
    if grad_recommendations:
        grad_context = _build_grad_context(grad_recommendations)
        # Insert grad data as a high-priority system message right after the main system prompt
        messages.insert(2, {"role": "system", "content": f"【重点数据】以下是本地数据库查询到的真实考研院校数据，请严格基于这些数据生成推荐。不要编造分数和学校信息。\n\n{grad_context}"})

    response = client.chat.completions.create(
        model=os.getenv("模型平台", "模型名称"),
        messages=messages,
    )

    answer = _extract_content(response)

    # Attach structured grad recommendations if available
    result = {
        "answer": answer,
        "research_type": profile.research_type,
        "plan": profile.plan,
        "evidence": profile.evidence,
        "key_findings": profile.key_findings,
        "next_steps": profile.next_steps,
        "sources": sources,
        "tool_used": bool(sources),
    }

    if grad_recommendations:
        result["grad_recommendations"] = grad_recommendations

    return result
