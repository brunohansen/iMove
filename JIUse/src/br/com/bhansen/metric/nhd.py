
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
    return (1 - ((2 / (l * k * (k - 1))) * n))

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
    return (1 - ((2 / (l * k * (k - 1))) * n))

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
	l = len(m[0])
	n = 0
	for i in range(k - 1):
		for i2 in range(i + 1, k):
			a = d = 0
			for j in range(l):
				c = m[i][j] + m[i2][j]
				if c == 2:
					a = a + 1
				if c == 1:
					d = d + 1
			n = n + (a / (a + d))
	return n / ((k * (k - 1)) / 2)

def pp(m):
	k = len(m)
	l = len(m[0])
	n = 0
	for j in range(l - 1):
		for j2 in range(j + 1, l):
			a = d = 0
			for i in range(k):
				c = m[i][j] + m[i][j2]
				if c == 2:
					a = a + 1
				if c == 1:
					d = d + 1
			n = n + (a / (a + d))
	return n / ((l * (l - 1)) / 2)

def im(m):
	k = len(m)
	l = len(m[0])
	n = 0
	for i in range(k - 1):
		for i2 in range(i + 1, k):
			for j in range(l):
				c = m[i][j] + m[i2][j]
				if c == 2:
					n = n + 1
					break
	return n / ((k * (k - 1)) / 2)

def ip(m):
	k = len(m)
	l = len(m[0])
	n = 0
	for j in range(l - 1):
		for j2 in range(j + 1, l):
			for i in range(k):
				c = m[i][j] + m[i][j2]
				if c == 2:
					n = n + 1
					break
	return n / ((l * (l - 1)) / 2)

def ic(m):
	r = ((cci(m) + pp(m)) / 2) * 0.5
	i = ((im(m) + ip(m)) / 2) * 0.5
	return r + i

a11 = [[1, 0], [0, 1]]
a12 = [[1, 0, 0], [0, 1, 0], [0, 0, 1]]

a21 = [[1, 0, 0, 0], [1, 1, 1, 1], [1, 1, 1, 1], [0, 0, 1, 0], [0, 0, 0, 1]]
a22 = [[0, 0, 1, 1], [1, 1, 0, 0], [0, 1, 1, 0], [1, 0, 0, 1], [0, 1, 1, 1]]

a31 = [[1, 0], [0, 1]]
a32 = [[1, 0, 0], [0, 1, 0], [0, 0, 1]]

a41 = [[1, 1, 1, 1], [1, 0, 0, 0], [1, 1, 1, 1], [0, 1, 1, 1]]
a42 = [[0, 0, 1, 0], [1, 0, 0, 0], [0, 0, 0, 1], [0, 1, 0, 0]]

vm1 = [[1, 0, 0, 0], [1, 0, 0, 0], [1, 0, 0, 0], [0, 1, 1, 1]]
vm2 = [[1, 0, 0, 0], [1, 0, 0, 0], [1, 1, 0, 0], [0, 1, 1, 1]]

a5a1 = [[1, 0, 1], [1, 1, 0], [0, 1, 1], [1, 0, 0]]
a5a2 = [[1, 0, 1], [1, 1, 0], [0, 1, 1], [1, 0, 0], [1, 0, 0]]

meu1 = [[1, 1, 1, 0], [0, 1, 1, 1], [0, 1, 1, 0]]

def cresce():
	m1 = [[1, 0, 1], [1, 1, 0], [0, 1, 1], [1, 0, 0]]
	print(ic(m1))
	for i in range(1, 10):
		m1.append([1, 0, 0])
		print(ic(m1))

def cresce2():
	m1 = [[1, 1, 1, 0], [0, 1, 1, 1], [0, 1, 1, 0]]
	print(nhdm(m1))
	for i in range(1, 10):
		m1.append([0, 1, 1, 0])
		print(nhdm(m1))
