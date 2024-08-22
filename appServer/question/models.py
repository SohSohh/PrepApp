from django.db import models


# Create your models here.
class Questions(models.Model):
    question = models.CharField(max_length=500, default="Question inaccessible")
    choices = models.JSONField(default=list)
    answer = models.CharField(max_length=200, default="Answer not present")
    subject = models.CharField(max_length=100, default="Subject inaccessible")

    def __str__(self):
        return self.question + " choices:" + str(self.choices) + " answer:" + self.answer + " subject:" + self.subject
