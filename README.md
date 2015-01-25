# mySD - Spin Dynamics Simulator

### Introduction
Classical spin dynamics simulator code written in Java.

The project aims to provide generic platform to run linear and nonlinear
classical spin dynamics simulaton of any system by solving Landau-Lifshitz-Gilbert equation.

(please refer to the following reference for more details : Eunsong Choi, Gia-Wei Chern, Natalia Perkins, Chiral magnetism and helimagnons in a pyrochlore antiferromagnet, Phys. Rev. B 87, 054418 (2013),
or http://arxiv.org/pdf/1301.5958v1.pdf )


### Usage (multi-threaded version)

A simulation can be run by typing the following line in a Linux system:


    java Run -nt [(optional)number of threads] -t [topology file] -s [run parameter file] -o [output file] -c [(optional)spin config file]


Number of threads need not be specified. Default vaule is the number of available processors in the system where the main program is executed.
Example of topology file(.top), run parameter file(.sdp), and spin configuration file(.cnf) are in the example folder.



Once simulation is done, spin-wave spectrum can be computed from output trajectory data(.trj file) using ComputeSpinWave.java code along with the .info file generated from the simulation. To do this, one can type in the following commands :

    java ComputeSpinWave -i [input name(need .info and .trj)] -t [topology file] -nk [number of k-space grids] -kx [bx direction component] -ky [by component] -kz [bz component] -nw [number of frequency space grids] -dw [size of each frequency grid] -o [output name]



### Before running the simulation 

To run a simulation, you need a topology file (.top) and a run parameter file (.sdp). Topology file contains information that completely defines a sytem to be simulated. Run-parameter file, on the other hand, defines a simulation specific parameters such as simulation length, simualtion type, output size, and etc. Optionally, a configuration file (.cnf) can be provided to overrides initial spin configurations. If .cnf file is not given, spin configurations defined in the .top file will be used. 

Below are examples of .top file and .sdp file. File formats are mostly self-explanatory, but will be explained in the tutorials section. Note that texts following # symbol are comments and not processed in the simulator code :


#### squareFM.top
    
    [ basis ]
    #number      x       y      z       (optional)Sx    Sy      Sz
         0     0.0     0.0    0.0                0.0   0.0     1.0 


    [ lattice_vector ]
    #ax
    1.0     0.0     0.0
    #ay
    0.0     1.0     0.0
    #az
    0.0     0.0     1.0


    [ unit_cells ]
    #nx     ny      nz
    50      5      5


    [ Hamiltonian ]
    #type       a11     a12     a13     a21     a22     a23     a31     a32     a33
    J1_FM      -1.0     0.0     0.0     0.0    -1.0     0.0     0.0     0.0    -1.0
    J1_AFM      1.0     0.0     0.0     0.0     1.0     0.0     0.0     0.0     1.0


    [ bonds ]
    #a           b       HamiltonianType
     0      <i+1>0       J1_FM
     0      <j+1>0       J1_FM
     0      <i-1>0       J1_FM
     0      <j-1>0       J1_FM
    



#### sdrun.sdp
    
    # Spin Dynamics Simulation run parameters 

    title                   = SquareLattice_FerroMagnet

    runtype                 = linear  #linear of nonlinear

    dt                      = 0.02  #time step size
    ntstep                  = 50000 #number of time steps
    alpha                   = 0.02  #Gilbert damping constant

    nstout                  = 5    #save outputs every nstout steps(0 to write only the final config)
    nstbuff                 = 100  #output buffer size(can enable this option using BufferedBasicWriter)
    nstenergy               = 0    #save total energy every nstenergy steps(only for nonlinear simulations) 

    perturb_site            = true #true to perturb a site at start
    perturbing_site_index   = 0    #index of a site to be perturbed
    perturbation_size       = 1    #linear case, this determines initial size of sigma_x,
                                   #nonlinear case, this is a roatation angle(degree)






### Linear vs Nonlinear 

Ths code supports both linear and non-linear simulation. Just specify either linear of nonlinear in the sdp file. Typically, nonlinear simulation can be used to find ground state spin configurations with the aid of large damping constant alpha, and linear simulation can be used to obtain clean spin fluctuations in the vicinity of ground state.



## Tutorials

Below are quick tutorials on how to use mySD package. All input files used in the tutorials can be found in /examples folder. 


###1. Square Lattice Ferromagnet

We fisrt need to write a topology file for this system. In this case, we only need one sub-lattice. Let's say each sub-lattice is placed at 0.0, 0.0, 0.0 and all spins are initially aligned along z-axis. This can be expressed as :
    
    [ basis ]
    #number      x       y      z       (optional)Sx    Sy      Sz
         0     0.0     0.0    0.0                0.0   0.0     1.0 
    

Then, we define Lattice vectors: ax, ay, and az. Here, we will simply put lattice vectors along with lab coordinates :
    
    [ lattice_vector ]
    #ax
    1.0     0.0     0.0
    #ay
    0.0     1.0     0.0
    #az
    0.0     0.0     1.0


The actual system consists of repeated basis along the lattice vectors. In the [ unit_cells ] section, we specify number of repeated units along each lattice vectors. Let's say later we will compute spin-wave spectrum along bx(reciprocal lattice vector of ax). In order to achieve good resolution of spin-wave along the desired direction, we need to place more repeated unit cells along this direction. I decided to put 50 along ax, and 5 for the other directions. 

    [ unit_cells ]
    #nx     ny      nz
    50      5      5


Now, we need to define a Hamiltonian. We first give a name to the Hamiltonian. In this case, we name it J1_FM meaning Ferro Magnetic interaction between nearest neighbors(though name is only used to look up matching interactions in the bonds section). Ferromagnetic Heigenberg interaction consists of negative diagonal components and zero off-diagonal components.


    [ Hamiltonian ]
    #type       a11     a12     a13     a21     a22     a23     a31     a32     a33
    J1_FM      -1.0     0.0     0.0     0.0    -1.0     0.0     0.0     0.0    -1.0


Finally, we need to sepcify all the i-j bond pairs in the system. Note that i-j bond and j-i bond must both be explicitly defined in this section (this will not results in double counting.) This allows us more flexibility in defining pair-wise interactions. For inter-unitcell interactions, i.e. bonds that cross unit cell border, you can specify relative position of corresponding unitcell in anglular brackets. For instance, "0    <i+1>1" indicates a bond between sub-lattice 0 at i, j, k-th unit cell and sub-lattice 1 at i+1, j, k-th unit cell. 


(to be updated...)

