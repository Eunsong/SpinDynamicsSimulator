import argparse
import re


def gen_neighbors(input_file, target_dist, rank, tol, search):
    # build basis
    basis = []
    lattice_vectors = []
    with open(input_file) as f:
        section = None
        for each_line in f:
            m = re.search(r'\[\s*([^\s]+)\s*\]', each_line)
            if m:
                section = m.group(1)
            if section == 'basis':
                m = re.search(r'(\d+)\s+([\d\.\-]+)\s+([\d\.\-]+)\s+([\d\.\-]+)\s+', each_line)
                if m:
                    index = int(m.group(1))
                    x = float(m.group(2))
                    y = float(m.group(3))
                    z = float(m.group(4))
                    basis.append( Base(index,Vector(x, y, z) ))
            elif section == 'lattice_vector':
                m = re.search(r'([\d\.\-]+)\s+([\d\.\-]+)\s+([\d\.\-]+)', each_line)
                if m:
                    x = float(m.group(1))
                    y = float(m.group(2))
                    z = float(m.group(3))
                    lattice_vectors.append(Vector(x,y,z)) 
    ax = lattice_vectors[0]
    ay = lattice_vectors[1]
    az = lattice_vectors[2]
    for base in basis :
        neighbors = []
        for i in xrange(-search,1+search):
            for j in xrange(-search,1+search):
                for k in xrange(-search,1+search):
                    shift = i*ax + j*ay + k*az
                    for neighbor in basis:
                        if neighbor == base and i == 0 and j == 0 and k == 0:    
                            continue
                        offset = ''
                        if i > 0:
                            offset += ('<i+'+str(i)+'>')
                        elif i < 0:
                            offset += ('<i-'+str(-i)+'>')
                        if j > 0:
                            offset += ('<j+'+str(j)+'>')
                        elif j < 0:
                            offset += ('<j-'+str(-j)+'>')
                        if k > 0:
                            offset += ('<k+'+str(k)+'>')
                        elif k < 0:
                            offset += ('<k-'+str(-k)+'>')
                        loc = shift + neighbor.r
                        rij = loc - base.r
                        distance = Vector.dot( rij, rij)
                        neighbors.append({'distance':distance, 'index':neighbor.index, 'offset':offset})
        if target_dist:
            neighbors = filter(lambda x : x['distance'] == target_dist, neighbors) 
        else:
            neighbors = get_nth_neighbors(neighbors, rank, tol)
        for neighbor in neighbors:
            #if _isvalid_ordering(base, neighbor, i, j, k):
            print('%4d  %15s%d'%(base.index,  neighbor['offset'], neighbor['index']))



def _isvalid_ordering(base, neighbor, i, j, k):
    if base.index > neighbor['index'] :
        return False 
    elif base.index == neighbor['index']:
        if i <= 0:
            return False
        elif j <= 0:
            return False
        elif k <= 0:
            return False
    return True



def get_nth_neighbors(neighbors, rank, tol):
    def _helper(neighbors, rank):
        dist = neighbors[0]['distance']
        newlst = filter(lambda x: abs(x['distance'] - dist) < tol, neighbors)
        if rank == 1:
            return newlst
        else:
            for neighbor in newlst:
                neighbors.remove(neighbor)
            return _helper(neighbors, rank-1) 
    sorted_neighbors = sorted(neighbors, key=lambda x: x['distance'])
    return _helper(sorted_neighbors, rank)
            


class Base(object):
    def __init__(self, index, position):
        self.index = index
        self.r = position

class Vector(object):
    def  __init__(self, x, y, z):
        self.x = x
        self.y = y
        self.z = z
    def __add__(self, other):
        return Vector(self.x + other.x, self.y + other.y, self.z + other.z)
    def __sub__(self, other):
        return Vector(self.x - other.x, self.y - other.y, self.z - other.z)
    def __mul__(self, other):
        return Vector(self.x * other, self.y * other, self.z * other)
    def __rmul__(self, other):
        return Vector(self.x * other, self.y * other, self.z * other)
    @staticmethod
    def dot(v1, v2):
        return v1.x*v2.x + v1.y*v2.y + v1.z*v2.z

if __name__ == '__main__':
    parser = argparse.ArgumentParser()
    parser.add_argument('topology', help = 'input topology file')
    parser.add_argument('--rank', '-r', default=1, type=int, help='n-th nearest neighbors')
    parser.add_argument('--distance', '-d', default=None, type=float,\
                        help='distance(squared) of bonds')
    parser.add_argument('--tolerance', '-t', default=0.00001, type=float,\
                        help='tolerance in distance comparison(to handle numerical \
                              errors involving square roots)')
    parser.add_argument('--search', '-s', default=1, type=int, help='look-up distance \
                        in unit cells along each direction for finding neighbors')
    arg = parser.parse_args()
    gen_neighbors(arg.topology, rank=arg.rank, target_dist=arg.distance, tol=arg.tolerance, search=arg.search)
