function display_last_step( plate, data_plate,plate_size, data_simulation,materialName, STEPS)
[XX YY] = meshgrid(linspace(0,data_plate.edge,plate_size),linspace(0,data_plate.edge,plate_size));
min_temp_value = min(min(min(plate)));
max_temp_value = max(max(max(plate)));
z_axis = [min_temp_value max_temp_value]; %each min/max reduce Dim by 1. 

%figure(1);
%pause(0.5);

%caxis(z_axis);%for color axis
%colorbar;

    figure();
    surf(XX,YY,plate(:,:,STEPS)); 
    shading interp;
    title(sprintf('Heat transfer of %s at time : %s[s]',materialName, num2str(STEPS*data_simulation.dt)));
    zlim(z_axis);
    
    xlabel('x coordinate');
    ylabel('y coordinate');
    zlabel('temperature');
    
    colorbar;
    caxis(z_axis);


end

