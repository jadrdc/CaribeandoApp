# Estado de implementación del rediseño — Caribeando

Revisión: 9 oct 2026. Compara `README.md` (handoff del 19 ago) con el código de `composeApp/src/commonMain` y `caribeando-backend` (cambios del 20–21 ago).

**Cómo se hizo:** lectura del código y de los comentarios fechados que dejó cada pasada ("design handoff pass", "fidelity pass", "dark-mode audit"). **No se compiló ni se probó la app.**

**Límite importante:** 359 de 461 archivos del proyecto están solo en iCloud (no descargados en el Mac) y no se pudieron leer. Entre ellos: `Colors.kt`, `Spacing.kt`, `ShoppingFlowNavigation.kt`, `ShoppingNavigationRoutes.kt`, `UpcomingOrders.kt`, `PayOrderBalanceUseCase.kt`, `OrderRepository.kt` y casi todo el backend. Las conclusiones sobre esas piezas se sacan de cómo las usan los archivos que sí se leyeron. Para revisarlas: en Finder, clic derecho en la carpeta `CaribeandoApp` → **Mantener descargado**.

## ✅ Cambios aplicados — 9 oct 2026 (segunda pasada)

**Nada de esto se ha compilado todavía.** La mayoría del proyecto sigue solo en iCloud y no se puede compilar desde aquí. Hay que compilar y probar en Android e iOS antes de publicar. Copias de seguridad de cada archivo modificado: en la sesión de Claude (no en tu carpeta).

### Pagos (lo crítico)
- **Error grave corregido (app):** "Pagar faltante" cobraba **el precio completo otra vez** en lugar de lo que faltaba (`PaymentViewModel` usaba `totalPayment`). Ahora cobra `balanceDue = total − pagado`. Archivos: `PaymentViewModel.kt`, `PayBalanceScreen.kt`.
- **Backend `POST /order/{id}/pay-balance`** (`OrderController.kt`):
  - Comprueba con Stripe que el PaymentIntent exista y esté `succeeded`.
  - Comprueba que el monto sea exactamente el faltante (en centavos, `monto × 100`).
  - Rechaza que se use el pago de la reserva original o un pago que ya se aplicó antes. Para esto marca el intent en Stripe con `metadata.caribeando_balance_order_id`, así que no hace falta cambiar la base de datos.
  - **Ya no sobrescribe `stripeTransaction`.** El código de confirmación del usuario ya no cambia y se conserva la referencia necesaria para reembolsar.
  - Aplica el límite de "hasta 24 h antes de la salida".
  - Si `Stripe.apiKey` no está configurado, responde 503 y no registra nada.
  - ⚠️ **Verificar antes de desplegar:** que `/payment/payment-sheet` convierta el monto a centavos como `monto × 100`. Si lo hace de otra forma, todos los pagos de faltante serán rechazados **después de cobrar la tarjeta**. También confirmar que `Stripe.apiKey` se configure al arrancar la app.
- **Checkout:** la opción marcada por defecto ahora es "Pago de reserva solamente", como pide el handoff.
- **Pago fallido:** se quitó "Guardamos tu lugar por 30 minutos" porque no era cierto. La app cancela la orden pendiente en cuanto falla el pago, así que el lugar no se guarda.

### Explorar / filtros (pantallas 7, 8 y 9)
- **Nueva pantalla Calendario** (`explore/screen/CalendarScreen.kt`):
  - Semana que empieza en domingo y 12 meses que se desplazan.
  - Rango marcado con círculos y texto de ayuda que cambia según el paso ("Toca el día de salida", etc.).
  - Botones "Borrar" y "Aplicar fechas".
  - Se abre desde "Fechas exactas" en la hoja de filtros.
- **Hoja de filtros:**
  - Chips de fecha: "Cualquier fecha" y los próximos 4 días de fin de semana. Un chip de día y un rango no pueden estar activos a la vez.
  - El rango elegido aparece como primer chip seleccionado.
- **"Borrar filtros"** (en la hoja y en la pantalla sin resultados) ahora limpia categoría, presupuesto, fechas y texto, cierra la hoja y recarga.
- **Botón principal:** muestra "Ver N resultados" solo cuando N es real, es decir, cuando los filtros elegidos coinciden con lo que ya se ve. Si hay cambios sin aplicar o no hay filtros, dice "Filtrar". No existe forma de saber cuántos resultados habrá antes de aplicar.
- **Errores corregidos de paso:**
  - Al bajar para cargar más viajes se usaban los filtros *sin aplicar* y siempre el rango de fechas por defecto, así que podían aparecer viajes que no cumplían el filtro. Ahora se usa el filtro aplicado.
  - Al cerrar la hoja se borraba el texto de búsqueda ya aplicado.

### Navegación
- **"Ver mis viajes"** en la pantalla "Listo" ahora abre la pestaña Viajes (`home/navigation/HomeTabRequest.kt`).
- **Transiciones** (`home/navigation/CaribeandoTransitions.kt`):
  - Al entrar o salir de una pantalla: 300 ms con la curva `cubic-bezier(.2,.8,.2,1)`, fundido y un pequeño deslizamiento. Aplica a la navegación principal, a Viajes y a Favoritos.
  - Entre pestañas: fundido.

### Pulido general
- **Margen lateral de 20dp** en Detalle, Checkout, Pagar faltante, Detalles de la reserva, Viajes, Calificar, Reportar, Perfil, Proveedor, Contáctanos, Favoritos, Explorar y Filtros. Las barras inferiores ahora tienen 14dp arriba y 18dp abajo.
- **Zona táctil de 48dp** en chips y en el botón "Calificar" (con `minimumInteractiveComponentSize`), sin cambiar su tamaño visual.
- **`PendingReview`:** el naranja fijo que no estaba en la paleta se cambió por los colores de la marca, que sí funcionan en modo oscuro.
- **Textos movidos a `strings.xml`:** Listo, Pago fallido, Error de página, Reportar, Detalles de la reserva, Pagar faltante, filtros y calendario.
- **Corrección del informe anterior:** la etiqueta "Mostrar" de la contraseña **ya existía**.

### Decisiones aplicadas (9 oct, tarde)
- **Cancelación con reembolso hasta 24 h antes de la salida**, igual en el backend (`REFUND_ELIGIBLE_HOURS_BEFORE_DEPARTURE = 24`) y en todos los textos de la app.
- **Reembolsos automáticos con Stripe** (`OrderController.cancelOrder` → `refundOrderPayments`):
  - Al cancelar una orden pagada dentro del plazo, se reembolsan el pago original y el pago del faltante, si lo hubo.
  - Es seguro reintentar: cada reembolso lleva una clave para que Stripe no lo duplique.
  - Si Stripe falla, la orden queda en `REFUND_PENDING` para que se reembolse a mano. Al usuario se le dice "en proceso", nunca "reembolsado".
  - Una orden `PENDING` (que nunca se cobró) solo se cancela, sin reembolso.
- **Datos de contacto desde el backend:**
  - Nuevo `GET /support/contact` (`SupportController.kt`, `ContactInfoDTO.kt`).
  - En la app: `ContactUsViewModel`, `OrderService.getContactInfo()` y `ContactInfoResponse`.
  - Se quitaron los datos de ejemplo. Los canales que vengan vacíos no se muestran.
  - ⚠️ **Falta configurar** en `application.properties` (o variables de entorno) del backend: `caribeando.support.phone`, `caribeando.support.whatsapp` (solo dígitos), `caribeando.support.email` y `caribeando.support.hours`. Mientras no estén, la pantalla muestra "No pudimos cargar los canales…" o ningún canal.

### Lo que sigue pendiente
1. **No se pudo editar porque los archivos siguen solo en iCloud:**
   - Corazón de favorito en la Galería (requiere `ShoppingFlowNavigation.kt`).
   - Revisar `Colors.kt` y `Spacing.kt`.
   - Transiciones del flujo de compra.
2. **Faltan datos en el backend:** "N personas", nota de la reseña, calificación del proveedor, "En Caribeando desde…", "duración" del viaje.
3. **Decisiones tuyas:**
   - Regla de cancelación: el detalle del viaje dice 24 h; el backend y la hoja de cancelar dicen 48 h.
   - Reembolsos automáticos con Stripe.
   - Tarjeta guardada o hoja de Stripe en el checkout.
   - Verificación de correo (pantalla 4).
   - Datos de contacto reales.
4. **Borrar archivos que ya no se usan:** `AmoutSliderPicker.kt`, `empty_state.png`, `book_error_2.xml`, logos duplicados. Necesito tu permiso para borrar.

---

# Análisis original (antes de esta pasada)


## Resumen

| Estado | Pantallas |
| --- | --- |
| ✅ Hechas (con pequeñas diferencias) | 1, 2, 5, 6, 10, 11, 12, 14, 15, 18, 22, 24, 25, 26, 27, 30 |
| 🟡 Hechas, pero les falta algo | 3, 4*, 7, 9, 13, 16, 17, 19, 20, 21, 23, 28, 29 |
| ❌ Sin hacer | 8 (Calendario) |

\* La 4 se resolvió con otro enfoque a propósito (ver abajo).

- **Tema y modo oscuro:** hechos. Están en `ExtendedColors.kt` (todos los colores en claro y oscuro), `Theme.kt` (`CaribeandoTheme` sigue el modo del sistema), `Radii.kt` y `TextStyles.kt`.
- **Backend:** se añadieron `POST /order/{id}/pay-balance` y `POST /order/{id}/cancel`, y `Order.amount` ahora guarda lo pagado de verdad. **Hay problemas serios en pay-balance; ver la sección "Riesgos".**

---

## ⚠️ Riesgos — revisar antes de publicar

1. **`pay-balance` confía en el cliente** (`OrderController.kt`, `payBalance`). El endpoint marca la orden como pagada por completo con cualquier `transactionId` que reciba. No comprueba con Stripe que el pago exista, que se haya cobrado ni que el monto sea correcto. Cualquier usuario podría saldar su reserva sin pagar. **Arreglo:** pedir el PaymentIntent a Stripe en el servidor y verificar `status == succeeded` y que el monto coincida con lo que falta por pagar.
2. **`pay-balance` sobrescribe `order.stripeTransaction`.** Se pierde el ID del pago de la reserva, que hace falta para reembolsarla. Además, `mapToOrderDTO` usa ese campo como `id`, y la app lo muestra como "Código de confirmación". **Resultado: el código que ve el usuario cambia después de pagar el faltante.** Arreglo: guardar el pago del faltante en un campo o tabla aparte.
3. **No se comprueba el plazo de pago.** El texto dice "el faltante se puede pagar aquí hasta 24 horas antes de la salida", pero el servidor no revisa la fecha de salida.
4. **Las reglas de cancelación se contradicen:**
   - El texto `cancellation_policy_details` (detalle del viaje) dice reembolso completo **hasta 24 h** antes.
   - El backend usa `REFUND_ELIGIBLE_HOURS_BEFORE_DEPARTURE = 48`.
   - La hoja de cancelar reserva dice **48 h**.
   
   Hay que decidir cuál es la regla y usarla en todos lados.
5. **La cancelación no reembolsa automáticamente.** La orden queda en `REFUND_PENDING` y alguien tiene que hacer el reembolso a mano en el panel de Stripe. Hace falta un proceso operativo, o conectar la llamada de reembolso a Stripe.
6. **Promesa no verificada en Pago fallido:** "Guardamos tu lugar por 30 minutos". No encontré lógica que reserve el lugar; puede estar en archivos que no se pudieron leer. Si no existe, quitar ese texto.
7. **Datos de contacto de ejemplo** en Contáctanos (`809 555 0100`, `hola@caribeando.do`), copiados del handoff. Hay que cambiarlos por los reales antes de publicar.

---

## Pantalla por pantalla

### Entrada
| # | Pantalla | Estado | Qué falta / notas |
| --- | --- | --- | --- |
| 1 | Iniciar sesión | ✅ | El campo de contraseña no tiene la etiqueta "Mostrar". |
| 2 | Completa tu cuenta | ✅ | Se mantuvo el campo de fecha de nacimiento (el backend lo exige; el handoff no lo incluía). Correcto. |
| 3 | ¿Olvidó su contraseña? | 🟡 | El toast aparece en esta pantalla y luego vuelve al login, en vez de mostrarse en el login. El resultado para el usuario es el mismo. |
| 4 | Cuenta sin confirmar | 🟡 (otro enfoque) | `Token.isConfirmed` significa "teléfono y fecha de nacimiento completos", no "correo verificado". Por eso se manda al usuario a completar el perfil en vez de cerrarle la sesión. No existe verificación de correo en el backend. **Decidir si se quiere crear.** |

### Explorar
| # | Pantalla | Estado | Qué falta / notas |
| --- | --- | --- | --- |
| 5 | Cargando | ✅ | Skeleton con el shimmer de la marca. |
| 6 | Explorar | ✅ | Tiene encabezado, contador y chips con "Todo". Sin "duración" (`TripModel` no tiene ese campo). |
| 7 | Filtrar | 🟡 | **Falta:** "Fechas exactas" con chips de fechas que se excluyen entre sí. "Ver N resultados" muestra el conteo del último filtro aplicado, no se actualiza mientras se eligen chips, y nunca vuelve a decir "Filtrar". El calendario sigue dentro de la hoja. |
| 8 | Calendario | ❌ | No se separó en su propia pantalla: sigue el `ModernDateRangePicker` dentro de la hoja de filtros. |
| 9 | Sin resultados | 🟡 | "Borrar filtros" solo quita la categoría. No limpia el presupuesto, las fechas ni el texto. |
| 10 | Error (modal) | ✅ | |
| 11 | Error de página | ✅ | Ya tiene `onRetry`. |

### Reservar
| # | Pantalla | Estado | Qué falta / notas |
| --- | --- | --- | --- |
| 12 | Detalles del viaje | ✅ | Foto de 300 de alto, contador tocable, barra fija abajo y margen inferior de 96. |
| 13 | Galería | 🟡 | Sin pie de foto (no existen esos datos) y **sin el corazón de favorito**. |
| 14 | Pagar viaje | 🟡 | **La opción seleccionada por defecto sigue siendo `TOTAL_PAYMENT`**; el handoff pide `BOOKING_PAYMENT` (`PaymentViewModel.kt`, línea ~281). No hay fila de "Información de pago" (pregunta abierta 3: tarjeta guardada o hoja de Stripe). |
| 15 | Procesando pago | ✅ | |
| 16 | Listo | 🟡 | "Ver mis viajes" vuelve a Inicio, **no abre la pestaña Viajes**. Falta pasar a qué pestaña ir por la navegación. |
| 17 | Pago fallido | 🟡 | Sin la fila "Método". Revisar el texto de los 30 minutos (riesgo 6). |

### Viajes
| # | Pantalla | Estado | Qué falta / notas |
| --- | --- | --- | --- |
| 18 | Próximos viajes | ✅ | Pestañas funcionando. Sin "N personas" (no existe ese dato). |
| 19 | Detalles de la reserva | 🟡 | El panel "Falta por pagar" ya usa datos reales. "Escríbele al proveedor" sigue desactivado (`if (true == false)`) y falta "N personas". |
| 20 | Pagar faltante | 🟡 | La pantalla y el endpoint existen, pero **ver los riesgos 1–3**. Sin la fila de "Información de pago". |
| 21 | Cancelar reserva | 🟡 | Usa la hoja inferior. Sin la fila "Reembolso" (el monto solo llega después de confirmar). Ver los riesgos 4–5. |
| 22 | Reportar inconveniente | ✅ | Al enviar se muestra el modal con el mensaje del backend en vez del toast. El string del toast existe pero no se usa. |
| 23 | Viajes pasados | 🟡 | Cuando ya está calificado se ve la estrella **sin la nota** (el backend solo devuelve `hasBeenEvaluated`). |
| 24 | Calificar viaje | ✅ | El comentario ahora es opcional. **No se ha probado cómo responde el backend a un comentario vacío.** |
| 25 | Viajes vacío | ✅ | |

### Cuenta
| # | Pantalla | Estado | Qué falta / notas |
| --- | --- | --- | --- |
| 26 | Favoritos | ✅ | Con el botón de quitar de favoritos. |
| 27 | Favoritos vacío | ✅ | |
| 28 | Perfil | 🟡 | Sin "En Caribeando desde…" (falta la fecha de alta en el backend). El perfil es de solo lectura (pregunta abierta 4). |
| 29 | Perfil del proveedor | 🟡 | Sin calificación ni número de reseñas del proveedor (`TripProviderModel` no tiene esos datos). |
| 30 | Contáctanos | ✅ | Los enlaces `tel:`/`mailto:`/WhatsApp funcionan, **pero los datos son de ejemplo** (riesgo 7). |

---

## Pendientes generales (no son de una sola pantalla)

- **Margen lateral de 20dp:** solo lo usan login, registro y recuperar contraseña. El resto sigue en **16dp** (Explorar, detalle, checkout, Viajes, Perfil…).
- **Transiciones:** no están las de entrar y salir de pantallas (300 ms, fundido + deslizamiento de 14 px), ni las del modal o la hoja. Solo hay animación en el toast y la barra inferior.
- **Zona táctil mínima de 44dp** en chips y botones pequeños: `minimumInteractiveComponentSize()` no se usa en ninguna parte.
- **Colores fijos que no cambian con el modo oscuro:** `PendingReview.kt` usa naranja (`#FFF3E0`/`#FF9800`), que no está en la paleta; `TripProviderProfileScreen` pone un borde blanco fijo al avatar. Hay otros `Color.White` sobre fondos rojos o azules que están bien.
- **Textos escritos directamente en Kotlin:** unos 25 textos fuera de `strings.xml` (Pago fallido, Listo, Pagar faltante, Cancelar reserva, "Ver N resultados", etc.). Pasarlos a recursos.
- **Limpieza:** `AmoutSliderPicker.kt` ya no se usa; `empty_state.png` (1.4 MB), `book_error_2.xml`, `caribeandoapp.png` e `ic_caribeando_logo.png` siguen en el repo; las fuentes Eau Sans tampoco se usan.
- **`OrderItemDetailsScreen.kt`** no se tocó desde 2025. En realidad es el detalle de un viaje desde Favoritos; la reserva se implementó en `ShoppingOrderDetailScreen.kt`. Confirmar si todavía se usa.

## Preguntas abiertas del handoff (siguen sin resolver)

1. Fotos reales.
2. "Duración" en el modelo del viaje.
3. Tarjeta guardada o hoja de Stripe en el checkout.
4. Perfil editable.
5. ~~Backend de pagar faltante~~: existe, pero con los riesgos 1–3.
6. Regla de cancelación: 24 h o 48 h.
7. Versión en inglés.

## Orden sugerido

1. Arreglar los riesgos 1–2 (verificar el pago con Stripe y no sobrescribir la transacción).
2. Unificar la regla de cancelación (riesgo 4) y decidir quién hace los reembolsos (riesgo 5).
3. Cambiar la opción de pago por defecto a `BOOKING_PAYMENT` y hacer que "Ver mis viajes" abra la pestaña Viajes.
4. Calendario como pantalla propia, "Fechas exactas", conteo en vivo y que "Borrar filtros" limpie todo.
5. Margen de 20dp, transiciones y zona táctil mínima.
6. Pasar los textos a `strings.xml` y borrar los recursos que ya no se usan.
7. Compilar y probar en Android e iOS, en modo claro y oscuro.
