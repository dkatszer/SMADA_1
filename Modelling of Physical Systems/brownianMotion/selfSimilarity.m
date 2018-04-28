% Self-similarity of totaly random numbers

clear;
clc;

c=[];
a=rand(1000,1);
% i - offset
% a(100:900) - removed head and tail in order to have the same length of 2
% vectors after shift
for i = -50:50
    c = [c corr(a(100:900), a(100+i:900+i))];
end
subplot(2,2,1);
plot(a);
title('X totally random');

subplot(2,2,2);
plot(c);
title('X NOT self-similar');

% Self-simalirity of brownian motion

steps = 1000;
displacement = randn(steps,2); 
trajectory = zeros(steps,2);

for i = 2:steps
  trajectory(i,:) = trajectory(i-1,:) + displacement(i,:);
end

Vx = trajectory(:,1);
result = [];
for j = -50:50
    result = [result corr(Vx(100:900), Vx(100+j:900+j))];
end

subplot(2,2,3);
plot(Vx);
title('X Brownian motion');

subplot(2,2,4);
plot(result);
title('X self-similar');
