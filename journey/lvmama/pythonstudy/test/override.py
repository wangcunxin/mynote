
class A:
    a1=100
    a2="kevin"
    def __init__(self):
        pass

    def __str__(self):
        return str(self.a1)+","+self.a2

    def __cmp__(self, other):
        if self.a1 > other.a1:
            return 1
        else:
            return -1

if __name__ == '__main__':
    a = A()
    print(a.__str__())
    aa = A()
    boo = cmp(a,aa)
    print boo