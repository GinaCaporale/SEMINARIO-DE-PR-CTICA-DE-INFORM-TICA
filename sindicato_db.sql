USE sindicato_db;

-- MODIFICACIÓN 1: Actualizar domicilio de un afiliado
UPDATE afiliado_jubilado 
SET domicilio = 'Mitre 456, Trelew'
WHERE id_afiliado = 1;

-- MODIFICACIÓN 2: Validar un pago pendiente
UPDATE pago 
SET estado = 'VALIDADO',
    id_usuario_valida = 3,
    fecha_validacion = NOW()
WHERE id_pago = 2;

-- MODIFICACIÓN 3: Cambiar estado de DJ a RECTIFICADA
UPDATE declaracion_jurada 
SET estado = 'RECTIFICADA'
WHERE id_dj = 3;

-- BORRADO 1: Eliminar un comprobante
DELETE FROM comprobante 
WHERE id_comprobante = 3;

-- BORRADO 2: Eliminar un afiliado inactivo
-- (primero lo damos de baja, nunca se borra un activo)
UPDATE afiliado_jubilado 
SET estado = 'INACTIVO' 
WHERE id_afiliado = 3;