clear;
clc;

DIM_PARAM = 3;
PARTICLES_NUMBER = 3;

steps = 10000;
displacement = randn(steps,DIM_PARAM,PARTICLES_NUMBER);
trajectory = zeros(steps,DIM_PARAM,PARTICLES_NUMBER);



for i = 2:steps
    trajectory(i,:,:) = trajectory(i-1,:,:) + displacement(i,:,:);
end

hold on;

for particle = 1:PARTICLES_NUMBER
    if DIM_PARAM == 1
        plot(trajectory(:,1,particle));
    elseif DIM_PARAM == 2
        plot(trajectory(:,1,particle), trajectory(:,2,particle));
    elseif DIM_PARAM == 3
        plot3(trajectory(:,1,particle), trajectory(:,2,particle), trajectory(:,3,particle));
    end    
end

hold off;


xlabel('x coordinate');
ylabel('y coordinate');