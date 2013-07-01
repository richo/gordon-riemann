# gordon-riemann

Tools for connecting [Edda][1] with [Riemann][2].


## What

Edda provides a unified interface over various AWS APIs. Riemann is a system
that collects and responds to infrastructure change events.

Gordon-riemann plugs these two systems together so that Edda feeds events into
Riemann. This lets us do things like autoscaling.


## How

The `gordon.edda` namespace provides the Edda-Riemann plumbing.

The `gordon.riemann` namespace defines custom Riemann handlers (the functions
that respond to events).


 [1]: https://github.com/Netflix/edda
 [2]: http://riemann.io/
