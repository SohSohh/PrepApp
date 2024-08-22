from django.urls import path, register_converter
from . import views, converters

register_converter(converters.NegativeIntConverter, 'negint')
urlpatterns = [
    path(
        "<negint:biology_n>/<negint:chemistry_n>/<negint:computers_n>/<negint:english_n>/<negint:intelligence_n>/<negint:mathematics_n>/<negint:physics_n>/",
        views.get_questions,
        name="question_access"
    ),
    path("limits/", views.get_limits_for_questions, name="qusetion_limits")
]


