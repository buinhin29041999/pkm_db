const fs = require('fs');

// Read CSV file
const csvData = fs.readFileSync('../pokedex.csv', 'utf-8');
const lines = csvData.split('\n');

// Initialize SQL file content
let sqlContent = 'INSERT INTO pokemon (\n' +
    'pokedex_number, name, german_name, japanese_name, generation, status, species,\n' +
    'type_number, type_1, type_2, height_m, weight_kg, abilities_number,\n' +
    'ability_1, ability_2, ability_hidden, total_points, hp, attack, defense,\n' +
    'sp_attack, sp_defense, speed, catch_rate, base_friendship, base_experience,\n' +
    'growth_rate, egg_type_number, egg_type_1, egg_type_2, percentage_male, egg_cycles,\n' +
    'against_normal, against_fire, against_water, against_electric, against_grass,\n' +
    'against_ice, against_fight, against_poison, against_ground, against_flying,\n' +
    'against_psychic, against_bug, against_rock, against_ghost, against_dragon,\n' +
    'against_dark, against_steel, against_fairy\n' +
    ') VALUES\n';

// Process each line
lines.slice(0).forEach((line, index) => {
    const columns = line.split(',');
    if (columns.length < 51) return; // Skip invalid lines

    // Map CSV columns to fields
    const values = [
        columns[1], // pokedex_number
        `'${columns[2].replace('\'', '\'\'')}'`, // name
        `'${columns[3]}'`, // german_name
        `'${columns[4]}'`, // japanese_name 
        columns[5], // generation
        `'0'`, // status
        `'${columns[7]}'`, // species
        columns[8], // type_number
        `${columns[9] === '' ? null : `(SELECT t.code FROM type t WHERE t.name = '${columns[9]}')`}`, // type_1
        `${columns[10] === '' ? null : `(SELECT t.code FROM type t WHERE t.name = '${columns[10]}')`}`, // type_2
        columns[11], // height_m
        columns[12], // weight_kg
        columns[13], // abilities_number
        `'${columns[14] === '' ? null : columns[14]}'`, // ability_1
        `'${columns[15] === '' ? null : columns[15]}'`, // ability_2
        `'${columns[16] === '' ? null : columns[16]}'`, // ability_hidden
        columns[17], // total_points
        columns[18], // hp
        columns[19], // attack
        columns[20], // defense
        columns[21], // sp_attack
        columns[22], // sp_defense
        columns[23], // speed
        `${columns[24] === '' ? null : columns[24]}`, // catch_rate
        `${columns[25] === '' ? null : columns[25]}`, // base_friendship
        `${columns[26] === '' ? null : columns[26]}`, // base_experience
        `'${columns[27] === '' ? null : columns[27]}'`, // growth_rate
        `${columns[28] === '' ? null : columns[28]}`, // egg_type_number
        `'${columns[29] === '' ? null : columns[29]}'`, // egg_type_1
        `'${columns[30] === '' ? null : columns[30]}'`, // egg_type_2
        `${columns[31] === '' ? null : columns[31]}`, // percentage_male
        columns[32], // egg_cycles
        columns[33], // against_normal
        columns[34], // against_fire
        columns[35], // against_water
        columns[36], // against_electric
        columns[37], // against_grass
        columns[38], // against_ice
        columns[39], // against_fight
        columns[40], // against_poison
        columns[41], // against_ground
        columns[42], // against_flying
        columns[43], // against_psychic
        columns[44], // against_bug
        columns[45], // against_rock
        columns[46], // against_ghost
        columns[47], // against_dragon
        columns[48], // against_dark
        columns[49], // against_steel
        columns[50]  // against_fairy
    ];

    sqlContent += `(${values.join(', ')})`;
    if (index < lines.length - 2) {
        sqlContent += ',\n';
    } else {
        sqlContent += ';\n';
    }
});

// Write SQL file
fs.writeFileSync('../sql/pokemon.sql', sqlContent);
console.log('SQL file generated successfully!');


// update pokemon
// set img_large = (case
// when name like 'Mega %' then concat(pokedex_number, '-mega.png')
// else concat(pokedex_number, '.png') end
// ),
// img_icon  = (case
// when name like 'Mega %' then concat(pokedex_number, '-mega.png')
// else concat(pokedex_number, '.png') end
// )
// where 1 = 1;
