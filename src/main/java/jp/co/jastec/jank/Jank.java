package jp.co.jastec.jank;

import jp.co.jastec.jank.cli.InteractiveLoop;
import jp.co.jastec.jank.input.Opening;
import jp.co.jastec.jank.model.JankModel;
import jp.co.jastec.jank.service.JankHome;

/// J社の日計入力をCLIで実装したらどうなるかを検証する目的で作成してみたアプリケーション
public class Jank {

    public static final void main(String args[]) {

        JankHome.init();

        /// InteractiveLoop というのが、このCLIの骨格となる仕組みで、
        /// 対話型入力を抽象かしている。
        InteractiveLoop loop = new InteractiveLoop(new Opening(new JankModel()));

        loop.start() ;

    }

}
