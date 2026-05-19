from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from routers.chat import router as chat_router

app = FastAPI(
    title="热讯 AI Agent",
    description="热点资讯 AI 汇总服务",
    version="1.0.0"
)

# ✅ 跨域配置，开发阶段放开所有来源
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],          # 允许所有来源
    allow_credentials=True,
    allow_methods=["*"],          # 允许所有方法
    allow_headers=["*"],          # 允许所有请求头
)

# 注册路由
app.include_router(chat_router)

@app.get("/")
def health_check():
    return {"status": "ok", "service": "hotpulse-ai"}
