clear;
clc;

dunaj = importdata('dunaj.prn'); %Real data
opady = importdata('opady.prn'); 

tt_start=32;
[val diff] = fminunc(@forward, tt_start);
tt = val(1);
Cin = opady(:,2);
C(:,1) = blackBox(Cin, tt);
C(:,2) = dunaj(:,2);
plot(1:size(opady), C)
legend('tt = 7.24', 'model');
xlabel('months');
ylabel('tritium');
%%
dunaj = importdata('dunaj.prn'); %Real data
opady = importdata('opady.prn'); 

tt_ar = [1 5 10 20];
for ii=1:4
    Cin = opady(:,2);
    C(:,ii) = blackBox(Cin, tt_ar(ii));
end
C(:,5) = dunaj(:,2);
plot(1:size(opady), C)
legend('tt = 1', 'tt = 5', 'tt = 10', 'tt = 20', 'model');
xlabel('months');
ylabel('tritium');
