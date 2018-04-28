function [ result ] = equastion_fraction( material, data_simulation)
    result = (material.conductivity * data_simulation.dt) / (material.heat * material.density * data_simulation.dx * data_simulation.dx);
end

