# Spin Dynamics Simulator

### Introduction
Classical spin dynamics simulator code written in Java.

The project aims to provide generic platform to run linear and nonlinear 
classical spin dynamics simulaton of any system by solving Landau-Lifshitz-Gilbert equation.

(please refer to the following reference for more details : Eunsong Choi, Gia-Wei Chern, Natalia Perkins, Chiral magnetism and helimagnons in a pyrochlore antiferromagnet, Phys. Rev. B 87, 054418 (2013),
or http://arxiv.org/pdf/1301.5958v1.pdf )


### Usage(multi-threaded simulation)

A simulation can be run by typing the following line in a Linux system:


    java Run -nt [(optional)number of threads] -t [topology file] -s [run parameter file] -o [output file] -c [(optional)spin config file]


Number of threads need not be specified. Default vaule is the number of available processors in the system where the main program is executed.
Example of topology file(.top), run parameter file(.sdp), and spin configuration file(.cnf) are in the example folder.



Once simulation is done, spin-wave spectrum can be computed from output trajectory data(.trj file) using ComputeSpinWave.java code. To do this, one can type in the following commands :

    java ComputeSpinWave -i [input name(need .info and .trj)] -t [topology file] -nk [number of k-space grids] -kx [bx direction component] -ky [by component] -kz [bz component] -nw [number of frequency space grids] -dw [size of each frequency grid] -o [output name]






### Linear vs Nonlinear 

Ths code supports both linear and non-linear simulation. Just specify either linear of nonlinear in the sdp file. Typically, nonlinear simulation can be used to find ground state spin configurations with the aid of large damping constant alpha, and linear simulation can be used to obtain clean spin fluctuations in the vicinity of ground state. 
