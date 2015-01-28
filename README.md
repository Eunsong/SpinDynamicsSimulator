# mySD - Spin Dynamics Simulator


![alt tag](https://raw.githubusercontent.com/Eunsong/SpinDynamicsSimulator/master/examples/figures/mySD_sample.jpg)



### Introduction
Classical spin dynamics simulator code written in Java.

The project aims to provide a generic platform to run linear and nonlinear
classical spin dynamics simulaton of any system by solving Landau-Lifshitz-Gilbert equation:


![alt tag](https://raw.githubusercontent.com/Eunsong/SpinDynamicsSimulator/master/examples/figures/LLGequation.png)


(please refer to the following reference for more details : Eunsong Choi, Gia-Wei Chern, Natalia Perkins, Chiral magnetism and helimagnons in a pyrochlore antiferromagnet, Phys. Rev. B 87, 054418 (2013),
or http://arxiv.org/pdf/1301.5958v1.pdf )


### Usage (multi-threaded version)

A simulation can be run by typing the following line in a Linux system:


    java Run -nt [(optional)number of threads] -t [topology file] -s [run parameter file] -o [output file] -c [(optional)spin config file]


Number of threads need not be specified. Default vaule is the number of available processors in the system where the main program is executed.
Example of topology file(.top), run parameter file(.sdp), and spin configuration file(.cnf) are in the example folder. Usage example can also be displayed by executing the code with --help (or -h) flag :


    java Run --help 


Run code generates four different output files: simulation information file (.info), spin trajectory file (.trj), final spin configuration file (.cnf) and system energy file (.eng). Note that .eng file is not generated for linear type simulations. All these files use same common file name defined with -o flag. For instance, "-o out" will create: out.info, out.trj, out.cnf, and out.eng. Once simulation is done, spin-wave spectrum can be computed from output trajectory data(.trj file) using ComputeSpinWave.java code along with the .info file generated from the simulation. To do this, one can type in the following commands :

    java ComputeSpinWave -i [input name(need .info and .trj)] -t [topology file] -nk [number of k-space grids] -kx [bx direction component] -ky [by component] -kz [bz component] -nw [number of frequency space grids] -dw [size of each frequency grid] -o [output name]


Similarly, usage example can be displayed with --help (or -h) flag :

    java ComputeSpinWave --help




### Before running the simulation 

To run a simulation, you need a topology file (.top) and a run parameter file (.sdp). Topology file contains information that completely defines a sytem to be simulated. Run-parameter file, on the other hand, defines simulation specific parameters such as simulation length, simualtion type, output size, and etc. Optionally, a configuration file (.cnf) can be provided to overrides initial spin configurations. If .cnf file is not given, spin configurations defined in the .top file will be used. 

Below are examples of .top file and .sdp file. File formats are mostly self-explanatory, but will be explained in the tutorials section. Note that texts following # symbol are comments and not processed in the simulator code :


#### topol.top
    
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

Below are quick tutorials on how to use mySD package. All input files used in the tutorials can be found in */examples* folder. 


###1. Square Lattice Ferromagnet

We fisrt need to write a topology file for this system. In this case, we only need one sub-lattice. Let's say each sub-lattice is placed at 0.0, 0.0, 0.0 and all spins are initially aligned along z-axis. This can be expressed as :
    
    [ basis ]
    #number      x       y      z       (optional)Sx    Sy      Sz
         0     0.0     0.0    0.0                0.0   0.0     1.0 
    

Then, we define Lattice vectors: ax, ay, and az. Here, we will simply put lattice vectors along the lab coordinates :
    
    [ lattice_vector ]
    #ax
    1.0     0.0     0.0
    #ay
    0.0     1.0     0.0
    #az
    0.0     0.0     1.0


The actual system consists of repeated basis along the lattice vectors. In the *[ unit_cells ]* section, we specify number of repeated units along each lattice vectors. Let's say later we will compute spin-wave spectrum along bx(reciprocal lattice vector of ax). In order to achieve good resolution of spin-wave along the desired direction, we need to place more repeated unit cells along this direction. I decided to put 50 along ax, and 5 for the other directions. 

    [ unit_cells ]
    #nx     ny      nz
    50      5      5


Now, we need to define a Hamiltonian. We first give a name to the Hamiltonian. In this case, we name it *J1_FM* meaning Ferro Magnetic interaction between nearest neighbors(though name is only used to look up matching interactions in the bonds section). Ferromagnetic Heigenberg interaction consists of negative diagonal components and zero off-diagonal components. So we can write it write it like this:


    [ Hamiltonian ]
    #type       a11     a12     a13     a21     a22     a23     a31     a32     a33
    J1_FM      -1.0     0.0     0.0     0.0    -1.0     0.0     0.0     0.0    -1.0



Note that an interaction matrix is defined as follows:

![alt tag](https://raw.githubusercontent.com/Eunsong/SpinDynamicsSimulator/master/examples/figures/AMatrix.png)

which then determines the Hamiltonian of the system:

![alt tag](https://raw.githubusercontent.com/Eunsong/SpinDynamicsSimulator/master/examples/figures/HamiltonianExpression.png)


Finally, we need to sepcify all the i-j bond pairs in the system. Note that i-j bond and j-i bond must both be explicitly defined in this section (this will not results in double counting.) This allows us more flexibility in defining pair-wise interactions. For inter-unitcell interactions, i.e. bonds that cross unit cell border, you can specify relative position of corresponding unitcell in anglular brackets. For instance, "0    < i+1 >1" indicates a bond between sub-lattice 0 at i, j, k-th unit cell and sub-lattice 1 at i+1, j, k-th unit cell. For square lattice, entire bonds can be defined as follows :

    [ bonds ]
    #a           b       HamiltonianType
     0      <i+1>0       J1_FM
     0      <j+1>0       J1_FM
     0      <i-1>0       J1_FM
     0      <j-1>0       J1_FM

This completes our topology file for simulating Ferromagnetic Square lattice. Put all the above in a file *topol.top*. 



We now need to define simulation specific parameters. First, let's name our simulation. The name of the simulation will be printed in .info file when simulation is done. And most importantly, we need to decide what type of simulation will be carried out. Since constructing a ground state configuration for Ferromagnetic state is straight forward(and is properly configured in our topology file constructed above), we will skip the nonlinear simulation and do linear simulation right away. 


    title                   = SquareLattice_FerroMagnet
    runtype                 = linear  #linear of nonlinear


Next, we need to define size of each time step, number of time steps, and Gilbert damping constant. There is no unique optimum value for these parameters. They depend on your system size and interactions. Generally, I would suggest to start with time step size of 0.02 to 0.1, and ten to hundereds of thousands of time steps. For damping constant, something around 0.01 would work find for linear simulation to obtain spin-wave, and around 0.4-0.5 for nonlinear simulation for obtaining ground state configuration. 


    dt                      = 0.02  #time step size
    ntstep                  = 50000 #number of time steps
    alpha                   = 0.02  #Gilbert damping constant


We now define how often we will output trajectories and energies. Trajectory will be saved in .trj file evey *nstout* steps. If *nstout* is set to 0, then trajectory won't be saved, but you will still get a final configuration in .cnf file. Since writing trajectory to a file is one most slow part in the entire simulation keeping this value as large as possible is a good way to optimize performace. In particular, if *dt* is small enough, saving trajectory each time step is probably unnecessary unless there is a very high-frequency mode. Here, I decided to save trajectory every 5 time steps meaning trajectory will be saved every 0.1 time. *nstenergy* define how often system energy will be saved in .eng file. This has nothing to do with linear simulations so you can just leave whatever value you like(I put 0 to explicitly show that we won't compute energies). *nstbuff* can be ignored for current version. 

    nstout                  = 5    #save outputs every nstout steps(0 to write only the final config)
    nstbuff                 = 100  #output buffer size(can enable this option using BufferedBasicWriter)
    nstenergy               = 0    #save total energy every nstenergy steps(only for nonlinear simulations) 


Finally, since we start from a ground state configuration, in order to generate fluctuations, we must perturb a site. Set *perturb_site = true* will enable initlal perturbation. Location of the perturbation can be specified in *perturbing_site_index*, but generally you will never have to change this index. Amount of perturbation can be specified in *perturbation_size*. For linear simulations, this value simply determines initial spin deviation size. For nonlinear simulations, this value is a rotation degree(in angles) of a specified site from the initial configuration. 

    perturb_site            = true #true to perturb a site at start
    perturbing_site_index   = 0    #index of a site to be perturbed
    perturbation_size       = 1    #linear case, this determines initial size of sigma_x,
                                   #nonlinear case, this is a roatation angle(degree)


We are done with the run parameters as well. Let's put them together in sdrun.sdp file. Or you can simply use example files in the */examples/squareFM* folder.  


Let's first check what inputs must be given to run a simulation. Type the following in the command line to display instruction of Run code :

    java Run --help

This will display required and optional inputs and appropriate flags to properly feed them to the code as follows.


    #####################################################################
    #                                                                   #
    #  Run.java is an executable back-bone code of mysd package         #
    # simulating classical spin dynamics on a periodic latice system.   #
    # checkout the following github repository for most recent updates: #
    # https://github.com/Eunsong/SpinDynamicsSimulator.git              #
    #                                                                   #
    # The code requires following inputs to run :                       #
    #          1. -t   .top file defining system topology               #
    #          2. -s   .sdp file listing simulation specific parameters #
    #          3. -c   (optional) .cnf file for overloading initial     #
    #                  spin configurations.                             #
    #          4. -o   desired common file name for outputs             #
    #                  (e.g. -o out will create out.info, out.trj,      #
    #                   and out.eng)                                    #
    #          5. -nt  (optional) number of threads to be used for      #
    #                  the simulation. If not specified, number of      #
    #                  currently available processors will be used.     #
    #                                                                   #
    # Usage example : java Run -t topology.top -s sdrun.sdp -c conf.cnf #
    #                -nt 8 -o out                                       #
    #                                                                   #
    #####################################################################


For our system, we can type something like this (assuming you have already compiled the entire package),

    java Run -t topol.top -s sdrun.sdp -o out

This will take roughly a minute or so depending on your computer and number of threads you use. In my computer, it took about 1m 10sec. Now, you will see three files are created in the same folder:

    out.info
    out.trj
    out.cnf


The system information file, *out.info*, contains the simulation parameters and lattice sites that have been used in the simulation. The trajectory file, *out.trj*, contains simply spin configurations recorded at specified intervals. The configuration file, *out.cnf*, is a final configuration(spin deviations for linear simulation), but is not much of use for a linear simulation. So we can simply delete it. 


Before computing spin-wave spectrum, let's check out what inputs are needed and how to feed them to the code by displaying usage instruction :

    java ComputeSpinWave --help

This will display the following :


    #####################################################################
    # ComputeSpinWave code computes spin-wave spectrum from trajectories#
    # of spins generated from mysd simulator. Check out the following   #
    # github repository for most recent version :                       #
    # https://github.com/Eunsong/SpinDynamicsSimulator.git              #
    #                                                                   #
    # The code requires following inputs to run :                       #
    #          1. -i   commond name of .info and .trj files             #
    #          2. -t   *.top  file                                      #
    #          3. -nk  number of desired k-space points(this must be    #
    #                  divisible of number of unit cells along desired  #
    #                  direction.)                                      #
    #          4. -kx, -ky, -kz  desired k-space direction              #
    #          5. -nw  number of desired w-space points                 #
    #          6. -dw  size of w-space grid                             #
    #          7. -o   output file name                                 #
    #                                                                   #
    # Usage example : java ComputeSpinWave -i inputs -t topology.top    #
    #                -nk 50 -kx 1 -ky 0 -kz 0 -nw 100 -dw 0.05          #
    #                -o spinwaves_cubicFM.dat                           #
    #                                                                   #
    ##################################################################### 


For our system, we can type in the following line to execute ComputeSpinWave code using output trajectory we have generated :

    java ComputeSpinWave -i out -t topol.top -nk 50 -kx 1 -ky 0 -kz 0 -nw 100 -dw 0.05 -o spinwave_squareFM.dat


Note that the flag *-i* followed by the file name *out* tells the code to look for *out.info* and *out.trj* files to re-build the lattice system and import the trajectory. *-nk* flag defines the number of k-space points to be computed along the direction specified by *-kx*, *-ky*, and *-kz* flags. The equation below shows how computed k-vectors are determined:


![alt tag](https://raw.githubusercontent.com/Eunsong/SpinDynamicsSimulator/master/examples/figures/kvector.png)


where m runs from 0 to nk. *-nk* and *-dw* determine the number of frequency space grids and the size of each grid respectively. Once computation is done, you can open the file *spinwave_squareFM.dat*. The file has three-column data which looks like this :

    # spin-wave spectrum computed from ComputeSpinWave.java code
    # computed at : 2015/01/22 16:07:56
    # check out https://github.com/Eunsong/SpinDynamicsSimulator for most recent updates
    #      k     omega  S[k][w]
      0.0000    0.0000  9.997e+03
      0.0000    0.0500  5.231e+01
      0.0000    0.1000  5.057e+01
         .         .        .
         .         .        .


where each column indicates k-value(*m/nk* precisely), frequency, and spin-deviation in k-w space. You can plot this file using your favorite plotting tool that supports either 3d plot or contour plot. Here I will plot it using gnuplot. If you go to */examples/squareFM/* folder, you can find a gnuplot script named *plotting_script*. You can simply execute this script to create a figure,

    gnuplot plotting_script

which will create a file named *spinwave_squareFM_k100.png* that looks like this,

<img src="https://github.com/Eunsong/SpinDynamicsSimulator/blob/master/examples/squareFM/spinwave_squareFM_k100.jpg" alt="Drawing" stype="width:400px;"/>


This completely the first tutorial. In the next tutorial, we will try slightly more complicated system and also see how non-linear simulation can be used.





(to be updated...)


