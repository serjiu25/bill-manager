# Object that stores extracted data
class ExtractedObject:
    def __init__(self, start, height, width, text, block, par, line, word, confidence):
        self.start = start
        self.height = height
        self.width = width
        self.text = text
        self.block = block
        self.par = par
        self.line = line
        self.word = word
        self.confidence = confidence

    @property
    def end(self):
        return self.right, self.bottom

    @property
    def left(self):
        return self.start[0]

    @property
    def top(self):
        return self.start[1]

    @property
    def right(self):
        return self.start[0] + self.width

    @property
    def bottom(self):
        return self.start[1] + self.height

    @staticmethod
    def from_tesseract(data):
        start = (data['left'], data['top'])
        height = data['height']
        width = data['width']
        text = data['text']
        block = data['block_num']
        par = data['par_num']
        line = data['line_num']
        word = data['word_num']
        confidence = data['conf']

        return ExtractedObject(start, height, width, text, block, par, line, word, confidence)