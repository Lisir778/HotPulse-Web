from typing import Literal

from pydantic import BaseModel, Field


class ChatMessage(BaseModel):
    role: Literal["user", "assistant"]
    content: str


class ChatRequest(BaseModel):
    query: str
    history: list[ChatMessage] = Field(default_factory=list)


class ResearchSource(BaseModel):
    id: int | None = None
    title: str
    summary: str | None = None
    source: str | None = None
    category: str | None = None
    heat: int | None = None
    published_at: str | None = None
    keyword: str | None = None


class GradRecommendation(BaseModel):
    schoolName: str | None = None
    schoolTag: str | None = None
    city: str | None = None
    majorName: str | None = None
    degreeType: str | None = None
    examSubjects: str | None = None
    totalScoreLine: int | None = None
    avgScore: int | None = None
    applicantCount: int | None = None
    admitCount: int | None = None
    admitRatio: float | None = None
    riskLevel: str | None = None
    reason: str | None = None
    tutors: list[str] = Field(default_factory=list)


class ChatResponse(BaseModel):
    answer: str
    research_type: str = "研究拆解"
    plan: list[str] = Field(default_factory=list)
    evidence: list[str] = Field(default_factory=list)
    key_findings: list[str] = Field(default_factory=list)
    next_steps: list[str] = Field(default_factory=list)
    sources: list[ResearchSource] = Field(default_factory=list)
    tool_used: bool = False
    grad_recommendations: list[GradRecommendation] | None = None
