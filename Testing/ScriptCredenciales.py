import csv
import random
import string

# Función para generar contraseñas aleatorias
def generar_contraseña(longitud=8):
    caracteres = string.ascii_letters + string.digits
    return ''.join(random.choice(caracteres) for i in range(longitud))

# Abrir el archivo CSV en modo escritura
with open('usuarios.csv', mode='w', newline='') as archivo:
    writer = csv.writer(archivo)
    writer.writerow(['correo', 'contraseña'])  # Escribir los encabezados del CSV
    
    # Generar 50 usuarios
    for i in range(50):
        correo = f'usuario{i+1}@example.com'  # Correo generado automáticamente
        contraseña = generar_contraseña()     # Contraseña aleatoria
        writer.writerow([correo, contraseña])  # Escribir la fila en el CSV
