%Set option -application - Structural mechanics plane strain
%Set axes limit [0,2] (X) [-0.5,0.5] (Y)
%stainles steel
    %PDE SPecyfication Poison, E, density for stainles steel
%20cm width
%1m length
%F = 1KN

%Options grid spacing - helping with drawing, we can zoom

%After drawing klik bonardy bound and then after klicking cornetr you could
%set forces (Neuman) at the right and top and bottom boundary should also
%be direchlecht but with 0 values . and places (second radiobutton) or
%Neuman
% force = g2 = -1000

%PDE Specyfication - put E=1.8E11 ,nu=0.305, rho=7480
%init mesh, refine mesh
%solve parameters  
%left adaptive and ok

%solve
%plot parameters - der=form parameter
%p property - different displacement changed the way of coloring and
%display different force displacement
%we can change color map


%1. compare with theoretical value of bend (export value to worskpace)
% nas interesuje y displacement (V)
%2. Simulate some more complex shape