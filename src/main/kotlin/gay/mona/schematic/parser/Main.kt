package gay.mona.schematic.parser

import net.sandrohc.schematic4j.SchematicLoader
import net.sandrohc.schematic4j.schematic.types.Pair
import java.nio.file.StandardOpenOption
import kotlin.io.path.Path
import kotlin.io.path.listDirectoryEntries
import kotlin.io.path.name
import kotlin.io.path.writer

object Main {

    fun <T> timed(name: String, init: () -> T): T {
        println("Starting $name")
        val time = System.currentTimeMillis()
        val t = init()
        println("Finished $name in ${System.currentTimeMillis() - time}ms")
        return t
    }

    @JvmStatic

    fun main(args: Array<String>) = timed("Mering all") {

        val list = Path("ch").listDirectoryEntries().map {
            timed(it.name) {
                SchematicLoader.load(it)
            }
        }

        Path("out.txt").writer(
            charset = Charsets.UTF_8,
            StandardOpenOption.TRUNCATE_EXISTING,
            StandardOpenOption.CREATE
        ).use {
            for (y in 20..200) {
                for (x in 0..630) {
                    for (z in 0..630) {
                        if (x == 0 && z == 0) {
                            val index = (396900 * y) + 630
                            println("${index / (79380000.toFloat()) * 100}% ($index / 79.380.000) ($x:$y:$z)")
                        }

                        val block = list.map { it.block(x, y, z) }.groupBy { it }.maxBy { it.value.size }.key
                        it.write("$x:$y:$z ${block.name}\n")
                    }
                }
            }

            //ergedSchematic.blocks().forEach { (pos, block) ->
            //   if (block == AIR) return@forEach
            //   it.write("${pos.x}:${pos.y}:${pos.z} ${block.name}\n")
            //
        }

    }

}