from fastapi import FastAPI
from api_v1.api import api_router

PROJECT_NAME = "FASTEN"
API_V1_STR = "/api"

app = FastAPI(title=PROJECT_NAME, openapi_url="/api/openapi.json")

app.include_router(api_router, prefix=API_V1_STR)
