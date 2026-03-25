package com.br.ponto_eletronico.Records.Relatorio;

import com.br.ponto_eletronico.entity.TipoRegistro;
import java.time.LocalDateTime;

public record MarcacaoPonto(
        LocalDateTime horario,
        TipoRegistro tipo
) {}
