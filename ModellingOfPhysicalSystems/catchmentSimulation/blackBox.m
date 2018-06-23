function Cout = blackBox( Cin, tt )
tmp = size(Cin);%opady and dunaj have the same size = 629
time = tmp(1);
C = zeros(time, 1);
T12 = 12.3; % T_1/2 half-life = 12.26 y
lambda = log(2)/(T12 * 12); %radioactive tritium decay constant
%lambda = 0.0565; %found in the internet

for t = 162:time % dunaj values are not 0 from 162 month
    suma = 0;
    for i = 1:t % i = t'
        f = Cin(i)*expoModel(tt,t,i)*exp(-lambda*(t-i));
        suma = suma + f;
    end
    C(t) = suma;
end

Cout = C;
end

