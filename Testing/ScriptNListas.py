import csv
import itertools

# Crear una lista de letras del abecedario sin incluir la 'R'
abecedario = [chr(i) for i in range(65, 91) if chr(i) != 'R']  # Letras mayúsculas A-Z menos 'R'

# Generar todas las combinaciones de 3 letras (puedes cambiar el tamaño según prefieras)
combinaciones = [''.join(comb) for comb in itertools.product(abecedario, repeat=3)]

# Guardar las combinaciones en un archivo CSV
nombre_archivo = 'productos.csv'
with open(nombre_archivo, mode='w', newline='', encoding='utf-8') as file:
    writer = csv.writer(file)
    writer.writerow(['producto'])  # Encabezado
    for combinacion in combinaciones:
        writer.writerow([combinacion])

print(f"Archivo '{nombre_archivo}' creado con éxito con {len(combinaciones)} nombres de listas.")
