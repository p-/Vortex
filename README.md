[![...](.resource/image/readme-header.png)](https://github.com/agustin-golmar/Vortex)
[![...](https://img.shields.io/badge/Java-v12-red.svg?logo=java&logoColor=white)](https://www.oracle.com/technetwork/java/javase/downloads/index.html)
[![...](https://img.shields.io/badge/Infer-v0.17.0-ffc210.svg?logo=facebook&logoColor=white)](https://fbinfer.com/)
[![...](https://www.travis-ci.com/agustin-golmar/Vortex.svg?branch=master)](https://www.travis-ci.com/agustin-golmar/Vortex)
[![...](https://snyk.io/test/github/agustin-golmar/Vortex/badge.svg?targetFile=pom.xml)](https://snyk.io/test/github/agustin-golmar/Vortex?targetFile=pom.xml)
[![...](https://img.shields.io/lgtm/alerts/g/agustin-golmar/Vortex.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/agustin-golmar/Vortex/alerts/)
[![...](https://img.shields.io/lgtm/grade/java/g/agustin-golmar/Vortex.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/agustin-golmar/Vortex/context:java)
[![...](https://app.fossa.com/api/projects/git%2Bgithub.com%2Fagustin-golmar%2FVortex.svg?type=small)](https://app.fossa.com/projects/git%2Bgithub.com%2Fagustin-golmar%2FVortex?ref=badge_small)

# Vortex

An implementation of the _FHP model_ (with 6 velocity degrees of freedom),
based on _cellular automatons_, for the simulation of a 2D fluid. This model
can solve the _Navier-Stokes equation_ for an incompressible flow.

## Build

You need _Apache Maven +3.6.0_, and _Java SE +12 Release_. Then run:

```bash
$ mvn clean package
```

## Configuration

The configuration must be in a file named `vortex.json`, located in the root
folder. It must contain the following properties:

| Property        | Description                                                                                               |
|:---------------:|-----------------------------------------------------------------------------------------------------------|
| `mode`          | The operation to run. It should be `simulation` or `animation`. |
| `output`        | The path to a folder where to put the generated files. |
| `lattice`       | The dimensions of the lattice in the form _\[columns, rows\]_. |
| `steps`         | The frames or steps to simulate. One frame implies a collision and propagation process. |
| `window`        | How many frames are computed before generating a velocity vector field. |
| `average`       | The size of the area taken for the velocity vector field. A value of _N_ implies an square area of _NxN_. |
| `seed`          | The seed of the _Mersenne Twister_ PRNG. This make the whole simulation reproducible. |
| `cores`         | How many cores or threads will be used for the simulation. A value of _-1_ creates one thread per core. |
| `cuda`          | __Not implemented.__ |
| `saveAutomaton` | __Not implemented.__ |
| `scenario`      | The properties of the scenario to simulate. It has many additional properties. |
| `bouncing`      | The type of reaction applied to a particle when hitting a solid node. Only supports the value `no-slip`. |
| `momentum`      | A list of velocity directions. The injected momentum will have this directions when applied. |
| `rate`          | The momentum will be injected every _rate_-frames. |
| `ratio`         | The momentum will be applied to a particle with a _ratio_ probability. |
| `sink`          | A list of geometries, where every geometry specifies a set of nodes that behave like sinkholes. |
| `solid`         | A list of geometries that specifies the solid nodes. |
| `source`        | A list of geometries that specifies the source of injected particles. |
| `geometry`      | The type of geometry to create. Only supports `line` for now. |
| `source`        | The coordinates of the starting node of the line. The line will include this node. |
| `destination`   | The coordinates of the ending node of the line. The line will include this node. |

This is a complete example of the configuration:

```
{
    "mode"          : "simulation",
    "output"        : ".resource/data/output",

    "lattice"       : [1920, 960],
    "steps"         : "100000",
    "window"        : 100,
    "average"       : 32,

    "seed"          : 35265826342033,
    "cores"         : 2,
    "cuda"          : false,
    "saveAutomaton" : false,

    "scenario"      : {
        "bouncing"  : "no-slip",
        "momentum"  : ["A", "B", "F"],
        "rate"      : 4,
        "ratio"     : 0.1,

        "sink"      : [{
            "geometry"      : "line",
            "source"        : [0, 1],
            "destination"   : [0, 958]
        }, {
            "geometry"      : "line",
            "source"        : [1919, 1],
            "destination"   : [1919, 958]
        }],
        "solid"     : [{
            "geometry"      : "line",
            "source"        : [0, 0],
            "destination"   : [1919, 0]
        }, {
            "geometry"      : "line",
            "source"        : [0, 959],
            "destination"   : [1919, 959]
        }],
        "source"    : [{
            "geometry"      : "line",
            "source"        : [1, 1],
            "destination"   : [1, 958]
        }]
    }
}
```

## Execution

In the root folder, just run:

```bash
$ java -jar vortex.jar
```

You can provide the following additional VM arguments (those prefixed with __-D__):

* `log.level`: any level supported by _Logback_ (default is _INFO_).
* `log.timezone`: any timezone supported by _Logback_ (default is _America/Argentina/Buenos\_Aires_).
* `vortex.config`: a path to the configuration file that should be used instead of the default location.

## License

[![...](https://app.fossa.io/api/projects/git%2Bgithub.com%2Fagustin-golmar%2FVortex.svg?type=large)](https://app.fossa.io/projects/git%2Bgithub.com%2Fagustin-golmar%2FVortex?ref=badge_large)

## Designer

This project has been built, designed and maintained by:

* [Agustín Golmar](https://github.com/agustin-golmar)

## Bibliography

__"Introduction To Practice Of Molecular Simulation"__. Akira Satoh. _Elsevier
Inc. ISBN 978-0-12-385148-2. Akita Prefectural University, Japan. 2011_.

__"Lattice Boltzmann Modeling"__. Michael C. Sukop, Daniel T. Thorne Jr.
_Springer-Verlag Berlin Heidelberg. ISBN 978-3-540-27982-2. USA. 2006_.

__"Lattice-Gas Cellular Automata And Lattice Boltzmann Models - An
Introduction"__. Dieter A. Wolf-Gladrow. _Springer. Alfred Wegener Institute
for Polar and Marine, Germany. June 26, 2005_.

__"Cellular Automata As Models Of Complexity"__. Stephen Wolfram. _Nature,
Vol. 311, N° 5985. Macmillan Journals, 1985. The Institute For Advanced Study,
Princeton, New Jersey, USA. October 4, 1984_.
