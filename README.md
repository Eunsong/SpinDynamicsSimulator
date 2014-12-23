# Spin Dynamics Simulator

### Introduction
Classical spin dynamics simulator code written in Java.

The project aims to provide generic platform to run linear and nonlinear 
classical spin dynamics simulaton of any system by solving Landau-Lifshitz-Gilbert equation.

(please refer to the following reference for more details : Eunsong Choi, Gia-Wei Chern, Natalia Perkins, Chiral magnetism and helimagnons in a pyrochlore antiferromagnet, Phys. Rev. B 87, 054418 (2013),
or http://arxiv.org/pdf/1301.5958v1.pdf )


### Usage

A simulation can be run by typing the following line in a Linux system:


    java Run -t [topology file] -s [run parameter file] -o [output file] -c [(optional)spin config file]


Example of topology file(.top), run parameter file(.sdp), and spin configuration file(.cnf) are in the example folder.



### Linear vs Nonlinear 

Ths code supports both linear and non-linear simulation. Just specify either linear of nonlinear in the sdp file. Typically, nonlinear simulation can be used to find ground state spin configurations with the aid of large damping constant alpha, and linear simulation can be used to obtain clean spin fluctuations in the vicinity of ground state. 
