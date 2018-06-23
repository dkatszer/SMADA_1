function diff = forward(tt)

dunaj = importdata('dunaj.prn'); %Real data
opady = importdata('opady.prn'); 
%The input function used in the modelling exercise is calculated as a mean value of tritium
%concentration in the precipitation (opady), weighted by the precipitation amount for each station

Cin = opady(:,2);
C = blackBox(Cin,tt);
diff = sqrt(sum((dunaj(:,2)-C).^2))

end

