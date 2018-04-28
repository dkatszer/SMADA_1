clear;
clc;

DIM = 2;
PARTICLES = 100;
STEPS = 1000;

displacement = randn(STEPS,DIM,PARTICLES);
trajectory = zeros(STEPS,DIM,PARTICLES);

for i = 2:STEPS
    %Just applying equation for different DIM and PARTICLES
    %It is sum of matrixes, thus we can wrote it one line for all dims and
    %particles
    trajectory(i,:,:) = trajectory(i-1,:,:) + displacement(i,:,:);
end


%% Just Displaying 
hold on;

for particle = 1:PARTICLES
    if DIM == 1
        plot(trajectory(:,1,particle));
    elseif DIM == 2
        plot(trajectory(:,1,particle), trajectory(:,2,particle));
    elseif DIM == 3
        plot3(trajectory(:,1,particle), trajectory(:,2,particle), trajectory(:,3,particle));
    end    
end
xlabel('x coordination');
ylabel('y coordination');
zlabel('z coordination');
hold off;

%% ONLY FOR TEST 
clc;
test_square = trajectory(2,:,:).^2;
test_sum = sum(test_square,2);
test_mean = mean(test_sum);
%% For good enough results STEPS = 10,000 ; PARTICLES = 1,000

%First element in trajectory is 0,0,0
meanDis = zeros(STEPS,1);
for s = 1:STEPS
    %trajectory(s,:,:).^2  -
        % in each step (rows no), 
        % square all elements (x,y,z) [depends on DIM]
        % do it for all particles
        % -- look at http://matematyka.pisz.pl/strona/1248.html
        % -- in order to understand what is happening
    %sum(A,2) - sum along rows, not colums .
        % 2 descibes dim along with elements will be sumarized 
        % trajectory is 3d (Steps, DIM[x,y,z], PARTICLES)
    %sum(trajectory(s,:,:).^2,2);
        % result of it is square distance from center for PARTICLE in
        % specific STEP
    %mean(sum(trajectory(s,:,:).^2,2));
        % mean along first dim not 1
        % step is 1 (specified)
        % DIM is 1 - rsult of sum function
        % PARTICLES is not 1
        % it will calculate mean distance from center for particles in step
    meanDis(s) = mean(sum(trajectory(s,:,:).^2,2));
end
plot(meanDis);
title('Particles mean square of displacement');
xlabel('step number');
ylabel('mean square of displacement');


%% Self Simalirity

Vx = trajectory(:,1,1);
result = [];
SHIFT = 50;
DOUBLE_SHIFT = 2 * SHIFT;
% We compare 2 vectors, element by element.
% If there is no shift then there will be always 1 as result, because
% values would be the same.
% If shift is greater then difference is also greater so correlation value
% is going down
for j = -SHIFT:SHIFT
    %Vx(100:900)  - window to compare
    %Vx(100+j:900+j) - x vector shifted 
    result = [result corr(Vx(DOUBLE_SHIFT:STEPS-DOUBLE_SHIFT), Vx(DOUBLE_SHIFT+j:STEPS-DOUBLE_SHIFT+j))];
end

subplot(2,2,1);
plot(Vx);
title('X vector of brownian motion ');
xlabel('Step number');
ylabel('x coordination');

subplot(2,2,2);
plot(result);
title('self-similar of brownian motion for X vector');
xlabel('shift value');
ylabel('correlation value');

randomResult=[];
randomX=rand(1000,1);
for i = -50:50
    randomResult = [randomResult corr(randomX(100:900), randomX(100+i:900+i))];
end
subplot(2,2,3);
plot(randomX);
title('vector X randomly generated');
xlabel('random value');
ylabel('Step number');

subplot(2,2,4);
plot(randomResult);
title('self-similarity of randomly generated vector');
xlabel('shift value');
ylabel('correlation value');

%% Density, good results for STEPS = 1,000 ; PARTICLES = 100

% histogram automatically create bins and count occurence in specific range
% X - bins with automatically selected range, steps
% Y - number of particles in specific bin
% in other words
    % It shows that at the begging all particles starts from the same x,
    % after few steps they are slowly move away from each other, and
    % finally each particle is in differnet bin.
xAllParticles = trajectory(:,1,:);
yAllParticles = trajectory(:,2,:);
subplot(1,2,1);
histogram(xAllParticles);
title('Particles density of x vector');
xlabel('x values');
ylabel('density value');

%figure(2);
%histogram(yAllParticles);


allX = reshape(xAllParticles, [STEPS*PARTICLES,1] );
allY = reshape(yAllParticles, [STEPS*PARTICLES,1] );

subplot(1,2,2);
hist3([allX, allY])
title('Particles density');
xlabel('x values');
ylabel('y values');
zlabel('density value');


%% ONLY FOR TEST hist3

% we provide x and y (floor) and z axis will described density
load seamount;
dat = [-y,x];
hist3(dat);