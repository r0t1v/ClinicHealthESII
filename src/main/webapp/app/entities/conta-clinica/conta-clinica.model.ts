import { IPaciente } from 'app/entities/paciente/paciente.model';
import { ITipoConvenio } from 'app/entities/tipo-convenio/tipo-convenio.model';
import { IExame } from 'app/entities/exame/exame.model';

export interface IContaClinica {
  id?: number;
  cpf?: string;
  senha?: string | null;
  cpf?: IPaciente | null;
  cpfs?: ITipoConvenio[] | null;
  cpfs?: IExame[] | null;
  cpf?: IPaciente | null;
}

export class ContaClinica implements IContaClinica {
  constructor(
    public id?: number,
    public cpf?: string,
    public senha?: string | null,
    public cpf?: IPaciente | null,
    public cpfs?: ITipoConvenio[] | null,
    public cpfs?: IExame[] | null,
    public cpf?: IPaciente | null
  ) {}
}

export function getContaClinicaIdentifier(contaClinica: IContaClinica): number | undefined {
  return contaClinica.id;
}
