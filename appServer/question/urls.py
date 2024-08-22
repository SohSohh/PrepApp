from django.urls import path
from . import views

urlpatterns = [
    path(
        "<int:biology_n>/<int:chemistry_n>/<int:computers_n>/<int:english_n>/<int:intelligence_n>/<int:mathematics_n>/<int:physics_n>/",
        views.get_questions,
        name="question_access"
    ),
    path("limits/", views.get_limits_for_questions, name="qusetion_limits")
]
