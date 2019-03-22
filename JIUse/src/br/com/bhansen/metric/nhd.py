
m = [[1,0,1],[1,1,0],[0,1,1],[1,0,0],[1,0,0]]

def cj(l, m):
    n = 0
    for i in range(len(m)):
        n = n + m[i][l]
    return n

def nhd(m):
    k = len(m)
    l = len(m[0])
    n = 0
    for i in range(l):
        c = cj(i,m)
        n = n + (c * (k - c))
    return (1 - ((2.0 / (l*k * (k-1))) * n))

def zj(l, m):
    n = 0
    for i in range(len(m)):
        n = n + ( 1 - m[i][l])
    return n

def nhdm(m):
    k = len(m)
    l = len(m[0])
    n = 0
    for i in range(l):
        c = cj(i,m)
        z = zj(i,m)
        n = n + (c * (k - c)) + (0.5 * (z * (z - 1)))
    return (1 - ((2.0 / (l*k * (k-1))) * n))

m = [[1,0,0,0,0],[1,0,0,0,0],[1,0,0,0,0],[1,0,0,0,0],[0,1,1,1,1]]



nhd(m)
0.38888888888888895

