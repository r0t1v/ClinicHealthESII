export interface IResultadoExame {
  id?: number;
  descricao?: string | null;
  prescricao?: string | null;
  indicacao?: string | null;
  contraindicacao?: string | null;
}

export class ResultadoExame implements IResultadoExame {
  constructor(
    public id?: number,
    public descricao?: string | null,
    public prescricao?: string | null,
    public indicacao?: string | null,
    public contraindicacao?: string | null
  ) {}
}

export function getResultadoExameIdentifier(resultadoExame: IResultadoExame): number | undefined {
  return resultadoExame.id;
}
