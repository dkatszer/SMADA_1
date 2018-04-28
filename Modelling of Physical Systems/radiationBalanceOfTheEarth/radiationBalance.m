%zad 1
clc;
clear;

global Pow_z ;
Pow_z = 4 * pi * earthRadius * earthRadius ; %Area of Earth surface
%INPUT DATA
global A ; 
A = 0.3; %mean albedo of Earth surface
global sigma ;
sigma = 5.670367 * 1E-8;% Stefan-Boltzman constant

S = 1361; %solar constant

tempForS = meanTempNoAtmosphere(S);

%Check for range of solar constant
S_range = linspace(0.75*S, 1.3*S, 20);

figure(1);
plot(S_range,arrayfun(@meanTempNoAtmosphere,S_range)-273.15);
xlabel('solar constant [W/m^2]');
ylabel('mean temperature [C]');
%Zad 2

global a_s;%surface albedo
a_s = 0.19; 
global S_var;
S_var = S;
fun = @meanTempWithAtmosphere;
x0 = [273.15,273.15];

S_range_inc = linspace(0.6*S, 1.4*S,20);
S_range_dec = fliplr(S_range_inc);

surf_inc = [];
surf_dec = [];
atmo_inc= [];
atmo_dec = [];

for i = 1:length(S_range_inc)
    S_var = S_range_inc(i);
    result = fsolve(fun,x0);
    result = result - 273.15;
    surf_inc = [surf_inc ; result(1)];
    atmo_inc = [atmo_inc ; result(2)];
    if result(1)>-5
		a_s=0.3;
    else
		a_s=0.63;
    end
end

for i = 1:length(S_range_dec)
    S_var = S_range_dec(i);
    result = fsolve(fun,x0);
    result = result - 273.15;
    surf_dec = [surf_dec ; result(1)];
    atmo_dec = [atmo_dec ; result(2)];
    if result(1)>-5
		a_s=0.3;
    else
		a_s=0.63;
    end
end

figure(2);
hold on;

plot(S_range_inc,surf_inc);
plot(S_range_inc,atmo_inc);
plot(S_range_dec,surf_dec);
plot(S_range_dec,atmo_dec);

xlabel('solar constant [W/m^2]');
ylabel('mean temperature [C]');

hold off;
legend('surface inc', 'atmosphere inc','surface dec', 'atmosphere dec');
result = fsolve(fun,x0);
result = result - 273.15;

