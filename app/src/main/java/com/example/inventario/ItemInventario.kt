package com.example.inventario

data class ItemInventario(
    var id:String="",
    var Nombre:String="",
    var Tipo_tela:String="",
    var Imagen:String="",
    var Precio:String="",
    var Ancho:String="",
    var Alto:String="",
    var stock_ideal: String="",
    var stock_actual: String=""
)
