function F = meanTempWithAtmosphere( X )

global S_var;
S = S_var;
global sigma;

%SHORT WAVE
global a_s;
t_a = 0.53; %transmission of the atmospher
a_a = 0.3; %albedo of the atmosphere
%LONG WAVE
t_a2 = 0.06; %transmission of the atmosphere
a_a2 = 0.31; %albedo of the atmosphere 
c = 2.7;

Ts = X(1);
Ta = X(2);

F(1) = ((-t_a)*(1-a_s)*S/4) + (c * (Ts - Ta)) + (sigma*(Ts^4)*(1-a_a2))-(sigma*(Ta^4));
F(2) = (-(1-a_a-t_a+a_s*t_a)*S/4)-(c*(Ts-Ta))-(sigma*(Ts^4)*(1-t_a2-a_a2))+(2*sigma*(Ta^4));
end

