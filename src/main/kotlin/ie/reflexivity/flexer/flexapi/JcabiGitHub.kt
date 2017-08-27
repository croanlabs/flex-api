package ie.reflexivity.flexer.flexapi

import com.jcabi.github.Coordinates
import com.jcabi.github.RtGithub
import com.jcabi.github.Github
import com.jcabi.github.Organization
import com.jcabi.github.Repo
import org.springframework.core.env.SystemEnvironmentPropertySource
import com.jcabi.github.wire.CarefulWire



private const val REPO = "pyethereum"
private const val USER = "ethereum"
private const val ORGANISATION = "ethereum"

fun main(args: Array<String>) {
    val github = RtGithub()
    val organisation = github.organizations().get(ORGANISATION)
    System.out.println("Name ${organisation.login()}");

    //Dont see a way from the organisation to drill down to a repo. So we would have to know a repo before hand. So not good :-(

    val coords = Coordinates.Simple(USER,REPO)
    val repo = github.repos().get(coords);

    repo.issues().iterate(emptyMap()).forEach {
        System.out.println("Issue Number ${it.number()}");
    }

}




