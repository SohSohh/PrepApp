from django.shortcuts import render
from django.http import JsonResponse
from django.http import HttpResponse
from django.core import serializers
from django.shortcuts import get_object_or_404
from .models import Questions
import random


# Create your views here.
def get_questions(request,biology_n:int, chemistry_n:int, computers_n:int, english_n: int, intelligence_n:int, mathematics_n:int, physics_n: int):
    allQuestions = list(Questions.objects.values("question", "choices", "answer", "subject"))
    questions_per_subject = [biology_n, chemistry_n, computers_n, english_n, intelligence_n, mathematics_n, physics_n]
    subjects = ["Biology", "Chemistry", "Computers", "English", "Intelligence", "Mathematics", "Physics"]
    data = []
    for i, subject_quantity in enumerate(questions_per_subject):
        filtered_list = [question for question in allQuestions if question["subject"] == subjects[i]]
        for _ in range(subject_quantity):
            if filtered_list:  # Ensure there's at least one question in the filtered list
                addedQuestion = random.choice(filtered_list)
                while addedQuestion in data:
                    addedQuestion = random.choice(filtered_list)
                data.append(addedQuestion)

    return JsonResponse(data, safe=False, json_dumps_params={"indent": 4})

def get_limits_for_questions(request):
    subjects = ["Biology", "Chemistry", "Computers", "English", "Intelligence", "Mathematics", "Physics"]
    data = []
    allQuestions = list(Questions.objects.values("question", "choices", "answer", "subject"))
    for subject in subjects:
        count = len([question for question in allQuestions if question["subject"] == subject])
        data.append(count)
    return JsonResponse(data, safe=False, json_dumps_params={"indent":4})

