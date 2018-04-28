clear;
clc;

STEPS = 3000;
%Different plate's materials
data_alumina = struct('density',2700,'heat',900,'conductivity',237);
data_cooper = struct('density',8920,'heat',380,'conductivity',401);
data_stainless_steel = struct('density',7860,'heat',450,'conductivity',58);
materials = [data_cooper,data_alumina,data_stainless_steel];
materials_names = {'cooper', 'alumina', 'stainless steel'};

data_heater = struct('edge',0.2,'const_temp',80,'power',100,'working_time',10);
data_plate = struct('edge',1,'const_edge_temp',10, 'init_temp',20, 'thickness',0.002);
% distance per one step. If edge = 1 and step_distance is 0.05 then it will
% be divided into 20 pieces.
step_distance = 0.05;

heater_size = data_heater.edge / step_distance;
plate_size = data_plate.edge / step_distance ;

%top left corner of heater in 2d view
heater_location = floor((plate_size - heater_size) / 2) + 1;
heater_in_plate = heater_location:(heater_location+heater_size-1);

%set init temp of plate everywhere
plate(1:plate_size, 1:plate_size, 1:STEPS) = data_plate.init_temp; %initializing plate


data_simulation = struct('dx',step_distance,'dy',step_distance,'dt',0.1,'Nt',0,'NX',plate_size,'NY',plate_size);
data_simulation.Nt = STEPS / data_simulation.dt;


%%
%BOUNDARY 1

%initializing blue edge
plate(1:plate_size,1,:) = data_plate.const_edge_temp;
plate(1:plate_size,plate_size,:) = data_plate.const_edge_temp;
plate(1,1:plate_size,:) = data_plate.const_edge_temp;
plate(plate_size,1:plate_size,:) = data_plate.const_edge_temp;


for m = 1:length(materials)

s = 2;
mean_diff = 1;
while(mean_diff > 0.0003)%0.0003 choosen arbitrarly
%for s = 1:STEPS  %Small changes required for live simulation
    %initializing heater
    plate(heater_in_plate,heater_in_plate,s) = data_heater.const_temp;
    for i = 2:plate_size-1
        for j = 2:plate_size-1
            plate(i,j,s+1) = plate(i,j,s) + equastion_fraction(materials(m),data_simulation) * (plate(i+1,j,s) + plate(i,j+1,s) - 4 * plate(i,j,s) + plate(i-1,j,s) + plate(i,j-1,s));
        end 
    end
    mean_diff = mean(mean(abs(plate(:,:,s-1)-plate(:,:,s))));
    s = s + 1;
end

display_last_step(plate,data_plate,plate_size, data_simulation,materials_names{m},s);
end
%%
display_simulation(plate,data_plate,plate_size, data_simulation,STEPS);
%%
%BOUNDARY 2
for m = 1:1
    
s = 2;
mean_diff = 1;
while(mean_diff > 0.00001)%0.00001 choosen arbitrarly
%for s = 1:STEPS %Small changes required for live simulation
    %initializing heater
    if data_simulation.dt * s < 10
        deltaT_heater = deltaT_heater_equastion(materials(m),data_heater,data_plate.thickness,data_simulation);
        plate(heater_in_plate,heater_in_plate,s) = plate(heater_in_plate,heater_in_plate,s) + deltaT_heater;
    %uncomment if you want to check state of plate during heater shutdown
    %else
    %    break
    end
    for i = 2:plate_size-1
        for j = 2:plate_size-1
            plate(i,j,s+1) = plate(i,j,s) + equastion_fraction(materials(m),data_simulation) * (plate(i+1,j,s) + plate(i,j+1,s) - 4 * plate(i,j,s) + plate(i-1,j,s) + plate(i,j-1,s));
        end 
        plate(1,i,s+1) = plate(2,i,s);
        plate(plate_size,i,s+1) = plate(plate_size-1,i,s);
        plate(i,1,s+1) = plate(i,2,s);
        plate(i,plate_size,s+1) = plate(i,plate_size-1,s);
    end
    plate(1,1,s+1) = plate(2,2,s);
    plate(1,plate_size,s+1) = plate(2,plate_size-1,s);
    plate(plate_size,1,s+1) = plate(plate_size-1,2,s);
    plate(plate_size,plate_size,s+1) = plate(plate_size-1,plate_size-1,s);
    
    mean_diff = mean(mean(abs(plate(:,:,s-1)-plate(:,:,s))));
    s = s + 1;
end
%display_simulation(plate,data_plate,plate_size, data_simulation,s);
display_last_step(plate,data_plate,plate_size, data_simulation,materials_names{m},s);
mean_tmp = mean(mean(plate(:,:,s)))
end
%% STADABILITY

data_simulation.dt = 0.4;
STEPS = 1000;
data_simulation.dx = 0.01;

for s = 1:STEPS %Small changes required for live simulation
    %initializing heater
    if data_simulation.dt * s < 10
        deltaT_heater = deltaT_heater_equastion(materials(2),data_heater,data_plate.thickness,data_simulation);
        plate(heater_in_plate,heater_in_plate,s) = plate(heater_in_plate,heater_in_plate,s) + deltaT_heater;
    %uncomment if you want to check state of plate during heater shutdown
    %else
    %    break
    end
    for i = 2:plate_size-1
        for j = 2:plate_size-1
            plate(i,j,s+1) = plate(i,j,s) + equastion_fraction(materials(2),data_simulation) * (plate(i+1,j,s) + plate(i,j+1,s) - 4 * plate(i,j,s) + plate(i-1,j,s) + plate(i,j-1,s));
        end 
        plate(1,i,s+1) = plate(2,i,s);
        plate(plate_size,i,s+1) = plate(plate_size-1,i,s);
        plate(i,1,s+1) = plate(i,2,s);
        plate(i,plate_size,s+1) = plate(i,plate_size-1,s);
    end
    plate(1,1,s+1) = plate(2,2,s);
    plate(1,plate_size,s+1) = plate(2,plate_size-1,s);
    plate(plate_size,1,s+1) = plate(plate_size-1,2,s);
    plate(plate_size,plate_size,s+1) = plate(plate_size-1,plate_size-1,s);
end
display_simulation(plate,data_plate,plate_size, data_simulation,STEPS);
%%

%NUMERICAL STADABILITY
%CHECK TIME NEEDED FOR STABILISATION 
%find the criteria (border), relationship between KX,KY and resolution of
%the model and the dt. When the values are oscilating




% aim
% boundary 1 ,2 
% stability of algorithm and time neede to stabilizate
% 3rd task , comparing with theorethical value and heat dissapation

% Compare result when temerature stabilised to constant value in every
% pixel. 
% Delta Tt = Q / Cw * m = (P * Theat) / (Cw * V * ro) = (P * Theat) / (Cw * A^2 * h * ro)
% h - grubosc materialu, A^2 pole powierzchni plytki , ro - gestosc, Cw -
% cieplo wlasciwe materialu
% compare to
% Delta Tm z symulacji (jak bardzo wzrosla wrotsc boarda

%3rd task - simulate it in environment Q=Cexchange(Tij - To) for each node

%%

[XX YY] = meshgrid(0:dx:A,o:dy:A);

surf(XX,YY,T(:,:,n+1));
title(['Simulation time = ' num2str(n*dt) ' (s)']);
xlabel('x (m)');
ylabel('y (m)');
zlabel('Temperature (degC)');