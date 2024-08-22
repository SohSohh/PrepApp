# Generated by Django 5.1 on 2024-08-21 10:30

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('question', '0003_remove_questions_test'),
    ]

    operations = [
        migrations.RemoveField(
            model_name='questions',
            name='data',
        ),
        migrations.AddField(
            model_name='questions',
            name='answer',
            field=models.CharField(default='Answer not present', max_length=200),
        ),
        migrations.AddField(
            model_name='questions',
            name='choices',
            field=models.JSONField(default=list),
        ),
        migrations.AddField(
            model_name='questions',
            name='question',
            field=models.CharField(default='Question inaccessible', max_length=500),
        ),
        migrations.AddField(
            model_name='questions',
            name='subject',
            field=models.CharField(default='Subject inaccessible', max_length=100),
        ),
    ]
