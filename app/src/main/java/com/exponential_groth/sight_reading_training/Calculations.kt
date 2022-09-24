package com.exponential_groth.sight_reading_training


fun ArrayList<Double>.average() : Double {
    var sum = 0.0
    for (x in this) {
        sum += x
    }
    return sum / this.size.toDouble()
}
