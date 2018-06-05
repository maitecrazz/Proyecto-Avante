import { Usuario } from "./usuario";

export class Publicacion {
    constructor(
        public id: number,
        public titulo: string,
        public descripcion: string,
        public etiquetas: string,
        public fecha: string,
        public valoracion: number,
        public usuario : Usuario
    ) { }
}