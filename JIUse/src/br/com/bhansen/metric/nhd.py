
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
        c = cj(i, m)
        n = n + (c * (k - c))
    return (1 - ((2.0 / (l * k * (k - 1))) * n))


def zj(l, m):
    n = 0
    for i in range(len(m)):
        n = n + (1 - m[i][l])
    return n


def nhdm(m):
    k = len(m)
    l = len(m[0])
    n = 0
    for i in range(l):
        c = cj(i, m)
        z = zj(i, m)
        n = n + (c * (k - c)) + (0.5 * (z * (z - 1)))
    return (1 - ((2.0 / (l * k * (k - 1))) * n))
    
def camc(m):
    k = len(m)
    l = len(m[0])
    n = 0
    for i in range(l):
        c = cj(i, m)
        n = n + c
    return n / (k * l)
    
def cci(m):
	k = len(m)
	n = 0
	for i in range(k - 1):
		a = d = 0
		for j in range(len(m[0])):
			if m[i][j] + m[i + 1][j] == 2:
				a = a + 1
			if m[i][j] + m[i + 1][j] == 1:
				d = d + 1
		n = n + (a / (a + d))
	return n / ((k * (k - 1)) / 2)
	
def pp(m):
	p = len(m[0])
	n = 0
	for j in range(p - 1):
		a = d = 0
		for i in range(len(m)):
			if m[i][j] + m[i][j + 1] == 2:
				a = a + 1
			if m[i][j] + m[i][j + 1] == 1:
				d = d + 1
		n = n + (a / (a + d))
	return n / ((p * (p - 1)) / 2)
	
def ic(m):
	return (cci(m) + pp(m)) / 2
	
m = [[1, 0, 1], [1, 1, 0], [0, 1, 1], [1, 0, 0], [1, 0, 0]]
	
m = [[1, 0, 0, 0, 0], [1, 0, 0, 0, 0], [1, 0, 0, 0, 0], [1, 0, 0, 0, 0], [0, 1, 1, 1, 1]]

m = [[1, 1, 1, 0], [0, 1, 1, 1]]

m = [[1, 1, 1, 0], [0, 1, 1, 1], [0, 1, 1, 0]]

m = [[1, 1, 1, 0], [0, 1, 1, 1], [0, 1, 1, 0], [0, 1, 1, 0]]

m = [[1, 1, 1, 0], [0, 1, 1, 1], [0, 1, 1, 0], [0, 1, 1, 0], [0, 1, 1, 0]]

nhd(m)

