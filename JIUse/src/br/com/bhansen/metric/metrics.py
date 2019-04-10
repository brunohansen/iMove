from decimal import *

getcontext().prec = 3

def cj(l, m):
    n = Decimal(0)
    for i in range(len(m)):
        n = n + m[i][l]
    return n

def nhd(m):
    k = Decimal(len(m))
    l = Decimal(len(m[0]))
    n = Decimal(0)
    for i in range(int(l)):
        c = cj(i, m)
        n = n + (c * (k - c))
    return (1 - ((2 / (l * k * (k - 1))) * n))

def zj(l, m):
    n = Decimal(0)
    for i in range(len(m)):
        n = n + (1 - m[i][l])
    return n

def nhdm(m):
    k = Decimal(len(m))
    l = Decimal(len(m[0]))
    n = Decimal(0)
    for i in range(int(l)):
        c = cj(i, m)
        z = zj(i, m)
        n = n + (c * (k - c)) + (Decimal('0.5') * (z * (z - 1)))
    return (1 - ((2 / (l * k * (k - 1))) * n))

def camc(m):
    k = Decimal(len(m))
    l = Decimal(len(m[0]))
    n = Decimal(0)
    for i in range(int(l)):
        c = cj(i, m)
        n = n + c
    return n / (k * l)

def cci(m):
    k = Decimal(len(m))
    l = Decimal(len(m[0]))
    n = Decimal(0)
    for i in range(int(k) - 1):
        for i2 in range(i + 1, int(k)):
            a = d = Decimal(0)
            for j in range(int(l)):
                c = m[i][j] + m[i2][j]
                if c == 2:
                    a = a + 1
                if c == 1:
                    d = d + 1
            n = n + (a / (a + d))
    return n / ((k * (k - 1)) / 2)

def pp(m):
    k = Decimal(len(m))
    n = Decimal(0)
    for i in range(int(k)):
        mCopy = m.copy()
        mi = mCopy.pop(i)
        n = n + ppx(mCopy, mi)
    return n / k

def ppx(m, mx):
    k = Decimal(len(m))
    l = Decimal(len(m[0]))
    r = Decimal(0)
    lx = 0
    for mxj in range(int(l)):    
        if mx[mxj] == 1:
            lx = lx + 1
            n = Decimal(0)
            for j in range(int(l)):
                if j != mxj:
                    a = d = Decimal(0)
                    for i in range(int(k)):
                        c = m[i][j] + m[i][mxj]
                        if c == 2:
                            a = a + 1
                        if c == 1:
                            d = d + 1
                    if a + d > 0:
                        n = n + (a / (a + d))
            r = r + (n / (l - 1))
    return r / lx

def ic(m):
    return (cci(m) + pp(m)) / 2

def iscomi(m):
    k = Decimal(len(m))
    l = Decimal(len(m[0]))
    n = Decimal(0)
    for i in range(int(k) - 1):
        for i2 in range(i + 1, int(k)):
            ni = ni2 = a = d = Decimal(0)
            for j in range(int(l)):
                ni = ni + m[i][j]
                ni2 = ni2 + m[i2][j]
                c = m[i][j] + m[i2][j]
                if c == 2:
                    a = a + 1
                if c == 1:
                    d = d + 1
            n = n + (((2 * a) / (ni + ni2)) * ((a + d) / l))
    return n / ((k * (k - 1)) / 2)

def wcci(m):
    k = Decimal(len(m))
    l = Decimal(len(m[0]))
    n = Decimal(0)
    for i in range(int(k) - 1):
        for i2 in range(i + 1, int(k)):
            a = d = Decimal(0)
            for j in range(int(l)):
                c = m[i][j] + m[i2][j]
                if c == 2:
                    a = a + 1
                if c == 1:
                    d = d + 1
            n = n + ((a / (a + d)) * ((a + d) / l))
    return n / ((k * (k - 1)) / 2)

def wpp(m):
    k = Decimal(len(m))
    n = Decimal(0)
    for i in range(int(k)):
        mCopy = m.copy()
        mi = mCopy.pop(i)
        n = n + wppx(mCopy, mi)
    return n / k

def wppx(m, mx):
    k = Decimal(len(m))
    l = Decimal(len(m[0]))
    r = Decimal(0)
    lx = Decimal(0)
    for mxj in range(int(l)):	
        if mx[mxj] == 1:
            lx = lx + 1
            pp = Decimal(0)
            for j in range(int(l)):
                if j != mxj:
                    a = d = Decimal(0)
                    for i in range(int(k)):
                        c = m[i][j] + m[i][mxj]
                        if c == 2:
                            a = a + 1
                        if c == 1:
                            d = d + 1
                    if a + d > 0:
                        pp = pp + ((a / (a + d)) * ((a + d) / k))
            print("Error when just one have a parameter")
            r = r + (pp / (l - 1))
    return r / lx

def wic(m):
    return (wcci(m) + wpp(m)) / 2

a01 = [[1, 1], [1, 1]]
a02 = [[1, 1, 1], [1, 1, 1], [1, 1, 1]]

a11 = [[1, 0], [0, 1]]
a12 = [[1, 0, 0], [0, 1, 0], [0, 0, 1]]

a21 = [[1, 0, 0, 0], [1, 1, 1, 1], [1, 1, 1, 1], [0, 0, 1, 0], [0, 0, 0, 1]]
a22 = [[0, 0, 1, 1], [1, 1, 0, 0], [0, 1, 1, 0], [1, 0, 0, 1], [0, 1, 1, 1]]

a31 = [[1, 0], [0, 1]]
a32 = [[1, 0, 0], [0, 1, 0], [0, 0, 1]]

a41 = [[1, 1, 1, 1], [1, 0, 0, 0], [1, 1, 1, 1], [0, 1, 1, 1]]
a42 = [[0, 0, 1, 0], [1, 0, 0, 0], [0, 0, 0, 1], [0, 1, 0, 0]]

vm1 = [[1, 0, 0, 0], [1, 0, 0, 0], [1, 1, 0, 0], [1, 1, 1, 1]]
vm2 = [[1, 0, 0, 0], [1, 0, 0, 1], [1, 1, 0, 0], [1, 1, 1, 1]]

a5a1 = [[1, 0, 1], [1, 1, 0], [0, 1, 1], [1, 0, 0]]
a5a2 = [[1, 0, 1], [1, 1, 0], [0, 1, 1], [1, 0, 0], [1, 0, 0]]

a5b1 = [[1, 0, 1], [1, 1, 0], [0, 1, 1], [1, 0, 0], [1, 0, 0], [1, 0, 0]]
a5b2 = [[1, 0, 1], [1, 1, 0], [0, 1, 1], [1, 0, 0], [1, 0, 0], [1, 0, 0], [1, 0, 0]]

meu1 = [[1, 1, 1, 0], [0, 1, 1, 1], [0, 1, 1, 0]]

app1 = [[1, 0, 0, 0], [1, 0, 0, 0], [1, 0, 0, 0], [0, 1, 1, 1]]
app2 = [[1, 0, 0, 0], [0, 1, 1, 1]]

def test(m):    
    print("CAMC -> ", camc(m))
    print("CCi -> ", cci(m))
    print("IC -> ", ic(m))
    print("ISCOMi -> ", iscomi(m))
    print("NHD -> ", nhd(m))
    print("NHDM -> ", nhdm(m))
    print("WIC -> ", wic(m))

def cresce():
    m1 = [[1, 0, 1], [1, 1, 0], [0, 1, 1], [1, 0, 0]]
    print(ic(m1))
    for i in range(1, 100):
        m1.append([1, 0, 0])
        print(ic(m1))

def cresce2():
    m1 = [[1, 1, 1, 0], [0, 1, 1, 1], [0, 1, 1, 0]]
    print(ic(m1))
    for i in range(1, 10):
        m1.append([0, 1, 1, 0])
        print(ic(m1))

wic(app1)
