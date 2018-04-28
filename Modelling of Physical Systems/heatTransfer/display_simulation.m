function display_simulation( plate, data_plate,plate_size, data_simulation, STEPS)
[XX YY] = meshgrid(linspace(0,data_plate.edge,plate_size),linspace(0,data_plate.edge,plate_size));
min_temp_value = min(min(min(plate)));
max_temp_value = max(max(max(plate)));
z_axis = [min_temp_value max_temp_value]; %each min/max reduce Dim by 1. 

%figure(1);
%pause(0.5);

%caxis(z_axis);%for color axis
%colorbar;

for i = 1:10:STEPS-1
    figure(1);
    surf(XX,YY,plate(:,:,i)); 
    shading interp;
    title(strcat('Simulation time = ', num2str(i*data_simulation.dt), ' (s)'));
    zlim(z_axis);
    
    xlabel('x coordinate');
    ylabel('y coordinate');
    zlabel('temperature');
    
    colorbar;
    caxis(z_axis);
    
    drawnow;
end 

end

