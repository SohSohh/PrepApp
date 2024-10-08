
# CONVERTER FOR NEGATIVE VALUES, LOOK UP CUSTOM PATH CONVERTERS FOR DJANGO
class NegativeIntConverter:
    regex = '-?\d+'

    def to_python(self, value):
        return int(value)

    def to_url(self, value):
        return '%d' % value