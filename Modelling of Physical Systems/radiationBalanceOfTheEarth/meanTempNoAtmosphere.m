function [ result ] = meanTempNoAtmosphere( S )
%SET OF ENERGY BALANCE EQUASIONS (no atmosphere):
global Pow_z;
global A;
global sigma;

syms T;
P_sl=S*(Pow_z/4)*(1-A); %Power of solar radiation (short wave)
Pz=sigma*(T^4)*Pow_z; % Power of radiation emitted from Earth (long wave)

eqn = P_sl - Pz == 0;
solution = solve(eqn,T);
%solution(1) - is smaller than 0 REAL
%solution(3) - is not REAL number
%solution(4) - is not REAL number
result = double(solution(2));
end

