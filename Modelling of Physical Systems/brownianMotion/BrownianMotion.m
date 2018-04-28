clear;
clc;

steps = 10000;
displacement = randn(steps,2); 
trajectory = zeros(steps,2);

for i = 2:steps
  trajectory(i,:) = trajectory(i-1,:) + displacement(i,:);
end

plot(trajectory(:,1), trajectory(:,2))
xlabel('x coordinate');
ylabel('y coordinate');
