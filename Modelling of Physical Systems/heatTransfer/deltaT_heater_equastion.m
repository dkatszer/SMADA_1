function [ result ] = deltaT_heater_equastion( material, data_heater, plate_thickness, data_simulation)
    result = (data_heater.power * data_simulation.dt) / (material.heat * data_heater.edge * data_heater.edge * plate_thickness * material.density);
end

