package gay.mona.schematic.parser

import net.sandrohc.schematic4j.SchematicLoader
import net.sandrohc.schematic4j.schematic.LitematicaSchematic
import net.sandrohc.schematic4j.schematic.types.SchematicBlock
import net.sandrohc.schematic4j.schematic.types.SchematicBlock.AIR
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

    operator fun LitematicaSchematic.get(x: Int, y: Int, z: Int): SchematicBlock {
        val region = this.regions.first()
        return region.blockStatePalette[region.blockStates[region.posToIndex(x, y, z)]]
    }

    @JvmStatic
    fun main(args: Array<String>) = timed("Mering all") {

        val list = Path("ch").listDirectoryEntries().filter { it.name.endsWith("matic") }.map {
            timed(it.name) {
                SchematicLoader.load(it) as LitematicaSchematic
            }
        }

        Path("out.txt").writer(
            charset = Charsets.UTF_8,
            StandardOpenOption.TRUNCATE_EXISTING,
            StandardOpenOption.CREATE
        ).use {
            for (y in 20..200) {
                println("Processing Y: $y")
                for (x in 0..628) {
                    for (z in 0..628) {
                        val block = list
                            .map { schematic -> schematic[x, y, z] }
                            .groupBy { block -> block }
                            .maxBy { (block, values) -> if (block == AIR) values.size * 0.5f else values.size.toFloat() }
                            .key

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