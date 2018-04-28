riverLength = 100;
riverWidth = 5;
riverDepth = 1;

U = 0.1; %mean flow velocity
D = 0.01; %dispersion coefficient

dx = 0.1;
dt = 0.1; 

simulationTime = 1200;
riverSize = riverLength / dx;
volumeOfRiver = riverWidth * riverDepth  * riverLength;
volumeOfDxRiver  = riverWidth * riverDepth * dx;

wieghtOfTracer = 1; % amount of injected tracer 1kg

Ca = U * dt / dx;
Cd = D * dt / (dx * dx);

C_j = zeros(riverSize,simulationTime / dt); %second dimension is responsible for time
C_j(10/dx,1) = wieghtOfTracer / volumeOfDxRiver;% location of the injection point 10m at the begining of the simulation
% location of the measurement point 90m - for plot purpose

for n=1:simulationTime/dt - 1
    for j=3:riverSize-1
        C_j(j,n+1) = C_j(j,n) + (Cd*(1-Ca) - (Ca/6)*(Ca*Ca - 3*Ca + 2))*C_j(j+1,n) - (Cd*(2 - 3*Ca) - (Ca/2)*(Ca*Ca - 2*Ca - 1))*C_j(j,n) + (Cd*(1 - 3*Ca) - (Ca/2)*(Ca*Ca - Ca - 2))*C_j(j-1,n) + (Cd*Ca + (Ca/6)*(Ca*Ca - 1))*C_j(j-2,n);
    end
end
%%
plot1Data = C_j(90/dx,:); %90 - location of measurement
figure();
plot(plot1Data);
xt = get(gca, 'XTick');                                
set(gca, 'XTick', xt, 'XTickLabel', xt*dt)  
xlabel('Time [s]');
ylabel('Tracer concentration');
title('Tracer concentration in 90m of the river');

figure();
plot2Data = zeros(simulationTime / dt , 1);
for n  = 1: simulationTime / dt;
    plot2Data(n) = sum(C_j(:,n)) * volumeOfRiver;
end 

plot(plot2Data);
xt = get(gca, 'XTick');                                
set(gca, 'XTick', xt, 'XTickLabel', xt*dt)  
xlabel('Time [s]');
ylabel('Total mass [g]');
title('Total mass of the tracer in the river');