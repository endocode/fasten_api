from fastapi import APIRouter

from api_v1.endpoints import app

api_router = APIRouter()
api_router.include_router(app.router, tags=["app"])

# from app.api.api_v1.endpoints import items, login, users, utils
# api_router.include_router(login.router, tags=["login"])
# api_router.include_router(users.router, prefix="/users", tags=["users"])
# api_router.include_router(utils.router, prefix="/utils", tags=["utils"])
# api_router.include_router(items.router, prefix="/items", tags=["items"])
