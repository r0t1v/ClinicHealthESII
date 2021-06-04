import { IExame } from 'app/entities/exame/exame.model';

export interface IMedico {
  id?: number;
  crm?: number | null;
  nome?: number | null;
  graduacao?: string | null;
  atuacao?: string | null;
  nome?: IExame | null;
}

export class Medico implements IMedico {
  constructor(
    public id?: number,
    public crm?: number | null,
    public nome?: number | null,
    public graduacao?: string | null,
    public atuacao?: string | null,
    public nome?: IExame | null
  ) {}
}

export function getMedicoIdentifier(medico: IMedico): number | undefined {
  return medico.id;
}
