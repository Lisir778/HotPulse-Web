from fastapi import APIRouter, HTTPException

from models.schemas import ChatRequest, ChatResponse, ResearchSource, GradRecommendation
from services.agent import run_agent

router = APIRouter(prefix="/agent", tags=["Research Agent"])


@router.post("/chat", response_model=ChatResponse)
async def chat(body: ChatRequest):
    if not body.query.strip():
        raise HTTPException(status_code=400, detail="query 不能为空")

    try:
        result = run_agent(query=body.query, history=body.history)
        grad_recs = result.get("grad_recommendations")
        return ChatResponse(
            answer=result["answer"],
            research_type=result["research_type"],
            plan=result["plan"],
            evidence=result["evidence"],
            key_findings=result["key_findings"],
            next_steps=result["next_steps"],
            sources=[ResearchSource(**source) for source in result["sources"]],
            tool_used=result["tool_used"],
            grad_recommendations=[GradRecommendation(**r) for r in grad_recs] if grad_recs else None,
        )
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"AI 服务异常：{str(e)}")
